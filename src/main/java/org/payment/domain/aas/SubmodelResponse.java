package org.payment.domain.aas;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "AAS Submodel 조회 응답")
public record SubmodelResponse(
        @Schema(description = "Submodel 식별자")
        String id,
        @Schema(description = "Submodel idShort", example = "nameplate")
        String idShort,
        @Schema(description = "Submodel 표시명", example = "nameplate")
        String name,
        @Schema(description = "Submodel 주요 값", example = "{\"manufacturerName\":\"Roadpia\",\"manufacturerProductDesignation\":\"MTK01_F1_NI1_REC01\",\"serialNumber\":\"SN-MTK01-F1-NI1-REC01\"}")
        Map<String, Object> values,
        @Schema(description = "BaSyx 원본 Submodel JSON", implementation = Object.class, example = "{\"id\":\"MTK01_F1_NI1_REC01:nameplate\",\"idShort\":\"nameplate\"}")
        JsonNode raw
) {
}
