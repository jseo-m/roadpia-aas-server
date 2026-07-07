package org.payment.domain.aas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "라인 노드")
public record LineNodeResponse(
        @Schema(description = "라인 코드", example = "NI1")
        String code,
        @Schema(description = "라인 표시명", example = "NI1")
        String name,
        @Schema(description = "자산 유형 목록")
        List<AssetTypeNodeResponse> assetTypes
) {
}
