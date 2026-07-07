package org.payment.domain.operation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "AAS OperationData SubmodelElement")
public record OperationDataSubmodelElementResponse(
        @Schema(description = "AAS 모델 타입", example = "Property")
        String modelType,
        @Schema(description = "SubmodelElement idShort", example = "temperature")
        String idShort,
        @Schema(description = "값 타입", example = "xs:double")
        String valueType,
        @Schema(description = "측정값", implementation = Object.class, example = "42.5")
        Object value,
        @Schema(description = "Qualifier 목록")
        List<OperationDataQualifierResponse> qualifiers
) {
}
