package org.payment.service.operation;

import org.payment.domain.operation.OperationDataQualifierResponse;
import org.payment.domain.operation.OperationDataSubmodelElementResponse;
import org.payment.domain.operation.OperationDataSubmodelResponse;
import org.payment.domain.operation.OperationHistoryResponse;
import org.payment.domain.operation.PlatingLineElementRow;
import org.payment.mapper.operation.PlatingLineElementMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class OperationDataService {

    private static final int VOLTAGE_TYPE_INDEX = 3;
    private static final int CURRENT_TYPE_INDEX = 4;

    private final PlatingLineElementMapper platingLineElementMapper;

    public OperationDataService(PlatingLineElementMapper platingLineElementMapper) {
        this.platingLineElementMapper = platingLineElementMapper;
    }

    public OperationDataSubmodelResponse getCurrentOperationData(String assetCode) {
        List<PlatingLineElementRow> rows = platingLineElementMapper.findCurrentValuesByAssetCode(assetCode);
        if (rows.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "현재 운전 데이터를 찾을 수 없습니다. assetCode=" + assetCode
            );
        }

        OffsetDateTime measuredAt = OffsetDateTime.now();
        List<OperationDataSubmodelElementResponse> elements = rows.stream()
                .filter(row -> row.getPlatingNowValue() != null)
                .sorted(Comparator.comparingInt(PlatingLineElementRow::getPlatingElementTypeIndex))
                .map(row -> toSubmodelElement(row, measuredAt))
                .toList();

        return new OperationDataSubmodelResponse(
                assetCode + ":operationData",
                "operationData",
                "Submodel",
                "Instance",
                assetCode,
                elements
        );
    }

    public OperationHistoryResponse getOperationDataHistory(
            String assetCode,
            String metricCode,
            OffsetDateTime from,
            OffsetDateTime to
    ) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "과거 운전 데이터 DB 연동은 아직 구현되지 않았습니다.");
    }

    private OperationDataSubmodelElementResponse toSubmodelElement(
            PlatingLineElementRow row,
            OffsetDateTime measuredAt
    ) {
        return switch (row.getPlatingElementTypeIndex()) {
            case CURRENT_TYPE_INDEX -> property("CUR", row.getPlatingNowValue(), "A", measuredAt);
            case VOLTAGE_TYPE_INDEX -> property("VLT", row.getPlatingNowValue(), "V", measuredAt);
            default -> throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "지원하지 않는 plating_element_type_index=" + row.getPlatingElementTypeIndex()
            );
        };
    }

    private OperationDataSubmodelElementResponse property(
            String idShort,
            BigDecimal value,
            String unit,
            OffsetDateTime measuredAt
    ) {
        List<OperationDataQualifierResponse> qualifiers = new ArrayList<>();
        qualifiers.add(new OperationDataQualifierResponse("unit", "xs:string", unit));
        qualifiers.add(new OperationDataQualifierResponse("measuredAt", "xs:dateTime", measuredAt.toString()));

        return new OperationDataSubmodelElementResponse(
                "Property",
                idShort,
                "xs:double",
                value,
                qualifiers
        );
    }
}
