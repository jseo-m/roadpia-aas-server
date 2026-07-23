package org.roadpia.aas.domain.aas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "자산 유형 노드")
public record AssetTypeNodeResponse(
        @Schema(description = "자산 유형 코드", example = "REC")
        String code,
        @Schema(description = "자산 유형 표시명", example = "REC")
        String name,
        @Schema(description = "자산 목록")
        List<AssetNodeResponse> assets
) {
}
