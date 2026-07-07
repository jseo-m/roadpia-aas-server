package org.payment.domain.operation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "AAS OperationData Submodel 형식의 현재 운전 데이터")
public record OperationDataSubmodelResponse(
        @Schema(description = "Submodel 식별자", example = "MTK01_F1_NI1_REC01:operationData")
        String id,
        @Schema(description = "Submodel idShort", example = "operationData")
        String idShort,
        @Schema(description = "AAS 모델 타입", example = "Submodel")
        String modelType,
        @Schema(description = "Submodel kind", example = "Instance")
        String kind,
        @Schema(description = "자산 ID", example = "MTK01_F1_NI1_REC01")
        String assetCode,
        @Schema(description = "SubmodelElement 목록")
        List<OperationDataSubmodelElementResponse> submodelElements
) {
}
