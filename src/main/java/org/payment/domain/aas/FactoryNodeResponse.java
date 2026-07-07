package org.payment.domain.aas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "공장 노드")
public record FactoryNodeResponse(
        @Schema(description = "공장 코드", example = "MTK01")
        String code,
        @Schema(description = "공장 표시명", example = "MTK01")
        String name,
        @Schema(description = "공정/구역 목록")
        List<AreaNodeResponse> areas
) {
}
