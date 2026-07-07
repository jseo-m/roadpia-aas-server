package org.payment.domain.aas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "AAS 자산 트리 응답")
public record AasAssetTreeResponse(
        @Schema(description = "공장 단위 자산 트리")
        List<FactoryNodeResponse> factories,
        @Schema(description = "트리 생성 중 발생한 경고 목록")
        List<AasWarningResponse> warnings
) {
}
