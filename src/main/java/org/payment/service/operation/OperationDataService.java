package org.payment.service.operation;

import org.payment.domain.operation.OperationDataSubmodelResponse;
import org.payment.domain.operation.OperationHistoryResponse;

import java.time.OffsetDateTime;

public interface OperationDataService {

    OperationDataSubmodelResponse getCurrentOperationData(String assetCode);

    OperationHistoryResponse getOperationDataHistory(
            String assetCode,
            String metricCode,
            OffsetDateTime from,
            OffsetDateTime to
    );
}
