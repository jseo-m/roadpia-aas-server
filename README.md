# Roadpia AAS Server PoC

Eclipse BaSyx AAS Environment를 내부 AAS 표준 API 서버로 사용하고, Spring Boot가 외부 호출자용 Adapter/API Gateway 역할을 하는 PoC 프로젝트입니다.

## 구조

```text
aasx/                         기존 .aasx 파일 위치
basyx/aas-env.properties      BaSyx AAS Environment 설정
docker-compose.yml            BaSyx 컨테이너 실행 설정
src/main/java/org/payment/
  client/                     BaSyx REST 호출 WebClient
  config/                     WebClient, OpenAPI 설정
  controller/                 외부 제공 REST API
  dto/                        API 응답 DTO와 Swagger Schema
  exception/                  API 예외 처리
  parser/                     AAS ID/idShort 자산 코드 파서
  repository/                 OperationData 조회 인터페이스와 Mock 구현
  service/                    자산 조회, 트리 구성, 운전 데이터 서비스
```

## BaSyx 실행

기존 AASX 패키지는 아래 폴더에 둡니다.

```text
./aasx
```

BaSyx AAS Environment를 실행합니다.

```bash
docker compose up -d
```

BaSyx Swagger UI:

```text
http://localhost:8081/swagger-ui/index.html
```

BaSyx는 `./aasx` 폴더를 컨테이너 내부 `/application/aasx`로 마운트하고, `basyx/aas-env.properties` 설정으로 AASX 파일을 로딩합니다.

## Spring Boot 실행

Java 17이 필요합니다.

```bash
./gradlew bootRun
```

Windows PowerShell:

```powershell
.\gradlew.bat bootRun --args="--spring.profiles.active=maintech,local"
```

Spring Boot Swagger UI:

```text
http://localhost:8090/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8090/v3/api-docs
```

## 외부 제공 API

```http
GET /api/aas/assets
GET /api/aas/assets/tree
GET /api/aas/assets/{assetCode}
GET /api/aas/assets/{assetCode}/nameplate
GET /api/aas/assets/{assetCode}/technical-data
GET /api/aas/assets/{assetCode}/operation-data/current
GET /api/aas/assets/{assetCode}/operation-data/history
```

## 자산 ID 파싱 규칙

AAS `idShort` 또는 AAS `id`에서 아래 구조를 파싱합니다.

```text
factoryCode_areaCode_lineCode_assetType_assetCode
```

예:

```text
SMF_PLATING_A_LINE01_PUMP_P001
SMF_PLATING_A_LINE01_SENSOR_TDS001
SMF_PLATING_A_LINE02_RECTIFIER_R001
```

URI 형태도 지원합니다.

```text
https://main-tech.co.kr/aas/{factoryCode}/{areaCode}/{lineCode}/{assetType}/{assetCode}
```

규칙과 맞지 않는 AAS는 전체 API를 실패시키지 않고 `unknown` 그룹으로 분류하며, 응답의 `warnings`에 경고 정보를 담습니다.

## OperationData

현재값 API는 외부 응답도 AAS 구조에 맞춰 `OperationData` Submodel 형식으로 반환합니다.

```json
{
  "id": "MTK01_F1_NI1_REC01:operationData",
  "idShort": "operationData",
  "modelType": "Submodel",
  "kind": "Instance",
  "assetCode": "MTK01_F1_NI1_REC01",
  "submodelElements": [
    {
      "modelType": "Property",
      "idShort": "VLT",
      "valueType": "xs:double",
      "value": 12.5,
      "qualifiers": [
        {
          "type": "unit",
          "valueType": "xs:string",
          "value": "V"
        },
        {
          "type": "measuredAt",
          "valueType": "xs:dateTime",
          "value": "2026-06-26T14:10:00+09:00"
        }
      ]
    },
    {
      "modelType": "Property",
      "idShort": "CUR",
      "valueType": "xs:double",
      "value": 120.0,
      "qualifiers": [
        {
          "type": "unit",
          "valueType": "xs:string",
          "value": "A"
        },
        {
          "type": "measuredAt",
          "valueType": "xs:dateTime",
          "value": "2026-06-26T14:10:00+09:00"
        }
      ]
    }
  ]
}
```

현재 운전 데이터는 실행 프로필별 설정에서 자산 라인에 맞는 테이블과 metric을 선택해 조회합니다. 메인텍 설정은 `application-maintech.yml`에 있으며, 현재 매핑은 아래와 같습니다.

```text
NI1  -> platservice_1.plating_line_element
NI2  -> platservice_2.plating_line_element
CUSN -> platservice_3.plating_line_element
NISN -> platservice_4.plating_line_element
```

각 테이블에서 `plating_element_type_index` 3은 전압 `VLT`, 4는 전류 `CUR`로 응답합니다.

## 설정

Spring Boot는 기본적으로 BaSyx를 아래 주소로 호출합니다.

```yaml
basyx:
  base-url: http://localhost:8081
```

설정 파일:

```text
src/main/resources/application.yml
```

DB와 BaSyx 주소는 환경변수로 변경할 수 있습니다.

```powershell
$env:ROADPIA_DB_URL="jdbc:mysql://localhost:3306/platservice_4?serverTimezone=Asia/Seoul&characterEncoding=UTF-8"
$env:ROADPIA_DB_USERNAME="platservice"
$env:ROADPIA_DB_PASSWORD="password"
$env:BASYX_BASE_URL="http://localhost:8081"
.\gradlew.bat bootRun
```

로컬 PC 전용 DB 설정은 Git에 올리지 않는 `src/main/resources/application-local.yml`에 두고 아래처럼 실행합니다.

```powershell
.\gradlew.bat bootRun --args="--spring.profiles.active=maintech,local"
```

Windows에서 프로젝트 루트의 `run-local.bat`를 실행해도 같은 명령으로 서버를 시작합니다.

## 롤백 메모

이 작업 전 원본 파일 스냅샷은 `.codex-rollback/` 아래에 저장됩니다. 사용자가 `작업 롤백`을 요청하면 해당 스냅샷 기준으로 변경 파일을 되돌릴 수 있습니다.
