package org.roadpia.aas.domain.aas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "공정/구역 노드")
public record AreaNodeResponse(
        @Schema(description = "공정/구역 코드", example = "F1")
        String code,
        @Schema(description = "공정/구역 표시명", example = "F1")
        String name,
        @Schema(description = "라인 목록")
        List<LineNodeResponse> lines
) {
}
