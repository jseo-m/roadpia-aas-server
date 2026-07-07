package org.payment.domain.aas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "자산 트리의 개별 자산 노드")
public record AssetNodeResponse(
        @Schema(description = "AAS 식별자", example = "urn:aas:kr:mtk01:MTK01_F1_NI1_REC01")
        String aasId,
        @Schema(description = "AAS idShort", example = "MTK01_F1_NI1_REC01")
        String idShort,
        @Schema(description = "자산 ID", example = "MTK01_F1_NI1_REC01")
        String assetCode,
        @Schema(description = "화면 표시명", example = "MTK01_F1_NI1_REC01")
        String name,
        @Schema(description = "자산 처리 경고 목록")
        List<AasWarningResponse> warnings
) {
}
