package org.roadpia.aas.domain.aas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AAS 자산 처리 중 발생한 경고 정보")
public record AasWarningResponse(
        @Schema(description = "경고 유형을 식별하는 코드입니다.", example = "INVALID_AAS_ID")
        String code,
        @Schema(description = "경고 상세 메시지입니다.", example = "AAS ID가 규칙과 일치하지 않아 unknown 그룹으로 분류했습니다.")
        String message
) {
}
