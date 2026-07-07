package org.payment.domain.common;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "API 오류 응답")
public record ErrorResponse(
        @Schema(description = "오류 발생 시각")
        OffsetDateTime timestamp,
        @Schema(description = "HTTP 상태 코드", example = "500")
        int status,
        @Schema(description = "오류 메시지")
        String message,
        @Schema(description = "상세 오류 내용")
        String detail
) {
}
