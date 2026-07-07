package org.payment.domain.aas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "AAS 자산 정보")
public record AasAssetResponse(
        @Schema(description = "BaSyx Shell의 id 값입니다.", example = "urn:aas:kr:mtk01:MTK01_F1_NI1_REC01")
        String aasId,
        @Schema(description = "BaSyx Shell의 idShort 값입니다.", example = "MTK01_F1_NI1_REC01")
        String idShort,
        @Schema(description = "AAS idShort 또는 id에서 파싱한 공장 코드입니다. 형식: {factoryCode}_{areaCode}_{lineCode}_{assetType}{assetNumber}", example = "MTK01")
        String factoryCode,
        @Schema(description = "AAS idShort 또는 id에서 파싱한 공정/구역 코드입니다. 형식: {factoryCode}_{areaCode}_{lineCode}_{assetType}{assetNumber}", example = "F1")
        String areaCode,
        @Schema(description = "AAS idShort 또는 id에서 파싱한 라인 코드입니다. 형식: {factoryCode}_{areaCode}_{lineCode}_{assetType}{assetNumber}", example = "NI1")
        String lineCode,
        @Schema(description = "AAS idShort 또는 id의 마지막 토큰에서 숫자 앞 문자 부분만 파싱한 자산 유형 코드입니다. 예: REC01 -> REC", example = "REC")
        String assetType,
        @Schema(description = "AAS idShort 또는 id에서 파싱한 자산 코드입니다. 파싱에 실패하면 id 또는 idShort 기반 fallback 값이 사용됩니다.", example = "MTK01_F1_NI1_REC01")
        String assetCode,
        @Schema(description = "자산 표시명입니다. Shell displayName, nameplate Submodel, identification Submodel 순서로 조회하며 없으면 assetCode를 사용합니다.", example = "MTK01_F1_NI1_REC01")
        String name,
        @Schema(description = "AAS id/idShort 파싱 등 자산 처리 중 발생한 경고 목록입니다.")
        List<AasWarningResponse> warnings,
        @Schema(description = "자산 하위 AAS Submodel")
        AasAssetSubmodelsResponse submodels
) {
}
