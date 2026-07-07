package org.payment.domain.operation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "과거 OperationData 단일 지점")
public record OperationHistoryPointResponse(
        @Schema(description = "측정 시각")
        OffsetDateTime measuredAt,
        @Schema(description = "측정값")
        Object value,
        @Schema(description = "단위", example = "C")
        String unit
) {
}
