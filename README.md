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
.\gradlew.bat bootRun
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

현재값과 과거값 API는 아직 DB에 연결하지 않고 Mock 데이터로 응답합니다.

현재값 API는 외부 응답도 AAS 구조에 맞춰 `OperationData` Submodel 형식으로 반환합니다.

```json
{
  "id": "mock://operation-data/P001/current",
  "idShort": "OperationData",
  "modelType": "Submodel",
  "kind": "Instance",
  "assetCode": "P001",
  "submodelElements": [
    {
      "modelType": "Property",
      "idShort": "temperature",
      "valueType": "xs:double",
      "value": 42.5,
      "qualifiers": [
        {
          "type": "unit",
          "valueType": "xs:string",
          "value": "C"
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

DB 연동 시에는 `repository/OperationDataRepository` 구현체를 MyBatis 또는 JPA 기반 구현으로 교체하면 됩니다. Controller와 Service의 외부 계약은 유지하는 방향으로 설계되어 있습니다.

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
.\gradlew.bat bootRun --args="--spring.profiles.active=local"
```

## 롤백 메모

이 작업 전 원본 파일 스냅샷은 `.codex-rollback/` 아래에 저장됩니다. 사용자가 `작업 롤백`을 요청하면 해당 스냅샷 기준으로 변경 파일을 되돌릴 수 있습니다.
