package org.payment.domain.aas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "특정 AAS 자산 조회 응답")
public record AasAssetDetailResponse(
        @Schema(description = "AAS 자산 정보와 하위 Submodel")
        AasAssetResponse asset
) {
}
