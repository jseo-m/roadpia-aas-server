package org.roadpia.aas.domain.operation;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AAS SubmodelElement Qualifier")
public record OperationDataQualifierResponse(
        @Schema(description = "Qualifier 타입", example = "unit")
        String type,
        @Schema(description = "Qualifier 값 타입", example = "xs:string")
        String valueType,
        @Schema(description = "Qualifier 값", example = "C")
        String value
) {
}
