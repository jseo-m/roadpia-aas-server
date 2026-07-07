package org.payment.service.operation;

import org.payment.domain.operation.OperationDataQualifierResponse;
import org.payment.domain.operation.OperationDataSubmodelElementResponse;
import org.payment.domain.operation.OperationDataSubmodelResponse;
import org.payment.domain.operation.OperationHistoryPointResponse;
import org.payment.domain.operation.OperationHistoryResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Profile("mock")
public class MockOperationDataService implements OperationDataService {

    @Override
    public OperationDataSubmodelResponse getCurrentOperationData(String assetCode) {
        OffsetDateTime measuredAt = OffsetDateTime.now();
        return new OperationDataSubmodelResponse(
                assetCode + ":operationData",
                "operationData",
                "Submodel",
                "Instance",
                assetCode,
                List.of(
                        property("temperature", "xs:double", 42.5, "C", measuredAt),
                        property("operationStatus", "xs:string", "RUNNING", null, measuredAt),
                        property("motorSpeed", "xs:int", 1750, "rpm", measuredAt)
                )
        );
    }

    @Override
    public OperationHistoryResponse getOperationDataHistory(
            String assetCode,
            String metricCode,
            OffsetDateTime from,
            OffsetDateTime to
    ) {
        OffsetDateTime baseTime = OffsetDateTime.now().minusMinutes(30);
        String resolvedMetricCode = metricCode == null || metricCode.isBlank() ? "temperature" : metricCode;

        return new OperationHistoryResponse(
                assetCode,
                resolvedMetricCode,
                from,
                to,
                List.of(
                        new OperationHistoryPointResponse(baseTime, 40.1, "C"),
                        new OperationHistoryPointResponse(baseTime.plusMinutes(10), 41.3, "C"),
                        new OperationHistoryPointResponse(baseTime.plusMinutes(20), 42.5, "C")
                )
        );
    }

    private OperationDataSubmodelElementResponse property(
            String idShort,
            String valueType,
            Object value,
            String unit,
            OffsetDateTime measuredAt
    ) {
        List<OperationDataQualifierResponse> qualifiers = new ArrayList<>();
        if (unit != null && !unit.isBlank()) {
            qualifiers.add(new OperationDataQualifierResponse("unit", "xs:string", unit));
        }
        qualifiers.add(new OperationDataQualifierResponse("measuredAt", "xs:dateTime", measuredAt.toString()));

        return new OperationDataSubmodelElementResponse(
                "Property",
                idShort,
                valueType,
                value,
                qualifiers
        );
    }
}
