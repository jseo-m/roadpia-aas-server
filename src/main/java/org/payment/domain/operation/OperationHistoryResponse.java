package org.payment.domain.operation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "자산 과거 OperationData 응답")
public record OperationHistoryResponse(
        @Schema(description = "자산 ID", example = "MTK01_F1_NI1_REC01")
        String assetCode,
        @Schema(description = "OperationData 항목 코드", example = "temperature")
        String metricCode,
        @Schema(description = "조회 시작 시각")
        OffsetDateTime from,
        @Schema(description = "조회 종료 시각")
        OffsetDateTime to,
        @Schema(description = "과거 OperationData 목록")
        List<OperationHistoryPointResponse> points
) {
}
