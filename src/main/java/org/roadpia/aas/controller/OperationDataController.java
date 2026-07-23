package org.roadpia.aas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.roadpia.aas.domain.operation.OperationDataSubmodelResponse;
import org.roadpia.aas.domain.operation.OperationHistoryResponse;
import org.roadpia.aas.service.operation.OperationDataService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@Tag(name = "AAS 운전 데이터", description = "자산별 OperationData를 조회합니다. 현재값은 AAS Submodel 형식으로 응답합니다.")
@RestController
@RequestMapping("/api/aas/assets/{assetCode}/operation-data")
public class OperationDataController {

    private final OperationDataService operationDataService;

    public OperationDataController(OperationDataService operationDataService) {
        this.operationDataService = operationDataService;
    }

    @Operation(
            summary = "특정 자산의 현재 OperationData 조회",
            description = "자산의 현재 전류/전압 데이터를 DB에서 조회해 AAS OperationData Submodel 형식으로 반환합니다."
    )
    @GetMapping("/current")
    public OperationDataSubmodelResponse getCurrentOperationData(
            @Parameter(description = "자산 ID", example = "MTK01_F1_NI1_REC01", required = true)
            @PathVariable("assetCode") String assetCode
    ) {
        return operationDataService.getCurrentOperationData(assetCode);
    }

    @Operation(
            summary = "특정 자산의 과거 OperationData 조회",
            description = "자산의 과거 운전 데이터 이력을 조회합니다."
    )
    @GetMapping("/history")
    public OperationHistoryResponse getOperationDataHistory(
            @Parameter(description = "자산 ID", example = "MTK01_F1_NI1_REC01", required = true)
            @PathVariable("assetCode") String assetCode,
            @Parameter(description = "조회할 OperationData 항목 코드", example = "temperature")
            @RequestParam(value = "metricCode", required = false) String metricCode,
            @Parameter(description = "조회 시작 시각", example = "2026-06-25T00:00:00+09:00")
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @Parameter(description = "조회 종료 시각", example = "2026-06-25T23:59:59+09:00")
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to
    ) {
        return operationDataService.getOperationDataHistory(assetCode, metricCode, from, to);
    }
}
