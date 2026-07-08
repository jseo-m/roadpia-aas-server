package org.payment.domain.operation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "AAS OperationData SubmodelElement")
public record OperationDataSubmodelElementResponse(
        @Schema(description = "AAS 모델 타입", example = "Property")
        String modelType,
        @Schema(description = "SubmodelElement idShort입니다. 전류는 CUR, 전압은 VLT로 응답합니다.", example = "CUR")
        String idShort,
        @Schema(description = "값 타입", example = "xs:double")
        String valueType,
        @Schema(description = "측정값", implementation = Object.class, example = "42.5")
        Object value,
        @Schema(description = "Qualifier 목록")
        List<OperationDataQualifierResponse> qualifiers
) {
}
