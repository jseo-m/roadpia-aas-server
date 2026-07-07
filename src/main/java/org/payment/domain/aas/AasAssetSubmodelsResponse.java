package org.payment.domain.aas;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.payment.domain.operation.OperationDataSubmodelResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "AAS 자산 하위 Submodel 목록")
public record AasAssetSubmodelsResponse(
        @Schema(description = "Nameplate Submodel")
        SubmodelResponse nameplate,
        @Schema(description = "TechnicalData Submodel")
        SubmodelResponse technicalData,
        @Schema(description = "AAS OperationData Submodel 형식의 현재 운전 데이터")
        OperationDataSubmodelResponse operationData
) {
}
