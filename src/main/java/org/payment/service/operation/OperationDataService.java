package org.payment.service.operation;

import org.payment.config.OperationDataProperties;
import org.payment.domain.operation.OperationDataQualifierResponse;
import org.payment.domain.operation.OperationDataSubmodelElementResponse;
import org.payment.domain.operation.OperationDataSubmodelResponse;
import org.payment.domain.operation.OperationHistoryResponse;
import org.payment.domain.operation.OperationMetricRow;
import org.payment.mapper.operation.PlatingLineElementMapper;
import org.payment.service.aas.AasAssetIdParser;
import org.payment.service.aas.AasAssetIdParts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class OperationDataService {

    private static final Pattern SQL_IDENTIFIER = Pattern.compile("[A-Za-z][A-Za-z0-9_]*");

    private final PlatingLineElementMapper platingLineElementMapper;
    private final AasAssetIdParser aasAssetIdParser;
    private final OperationDataProperties operationDataProperties;

    public OperationDataService(
            PlatingLineElementMapper platingLineElementMapper,
            AasAssetIdParser aasAssetIdParser,
            OperationDataProperties operationDataProperties
    ) {
        this.platingLineElementMapper = platingLineElementMapper;
        this.aasAssetIdParser = aasAssetIdParser;
        this.operationDataProperties = operationDataProperties;
    }

    public OperationDataSubmodelResponse getCurrentOperationData(String assetCode) {
        AasAssetIdParts parts = aasAssetIdParser.parse(assetCode, assetCode);
        if (!parts.matched()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "자산 코드 형식이 올바르지 않습니다. assetCode=" + assetCode
            );
        }

        OperationDataProperties.Source source = resolveSource(parts);
        validateSource(source);

        List<OperationDataProperties.Metric> metrics = source.getMetrics().stream()
                .filter(metric -> metric.getTypeIndex() != null)
                .toList();
        if (metrics.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "OperationData metric 설정이 없습니다. assetCode=" + assetCode
            );
        }

        List<Integer> typeIndexes = metrics.stream()
                .map(OperationDataProperties.Metric::getTypeIndex)
                .toList();
        Map<Integer, OperationDataProperties.Metric> metricByTypeIndex = metrics.stream()
                .collect(Collectors.toMap(
                        OperationDataProperties.Metric::getTypeIndex,
                        Function.identity(),
                        (left, ignored) -> left
                ));

        List<OperationMetricRow> rows = platingLineElementMapper.findCurrentValuesByAssetCode(
                source.getSchemaName(),
                source.getTableName(),
                source.getAssetCodeColumn(),
                source.getTypeIndexColumn(),
                source.getValueColumn(),
                assetCode,
                typeIndexes
        );
        if (rows.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "현재 운전 데이터를 찾을 수 없습니다. assetCode=" + assetCode
            );
        }

        OffsetDateTime measuredAt = OffsetDateTime.now();
        List<OperationDataSubmodelElementResponse> elements = rows.stream()
                .filter(row -> row.getValue() != null)
                .filter(row -> metricByTypeIndex.containsKey(row.getTypeIndex()))
                .sorted(Comparator.comparingInt(row -> metricByTypeIndex.get(row.getTypeIndex()).getSortOrder()))
                .map(row -> toSubmodelElement(row, metricByTypeIndex.get(row.getTypeIndex()), measuredAt))
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
            OperationMetricRow row,
            OperationDataProperties.Metric metric,
            OffsetDateTime measuredAt
    ) {
        return property(
                metric.getIdShort(),
                row.getValue(),
                metric.getUnit(),
                firstNotBlank(metric.getValueType(), "xs:double"),
                measuredAt
        );
    }

    private OperationDataSubmodelElementResponse property(
            String idShort,
            BigDecimal value,
            String unit,
            String valueType,
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

    private OperationDataProperties.Source resolveSource(AasAssetIdParts parts) {
        return operationDataProperties.getSources().stream()
                .filter(source -> matches(source.getFactoryCode(), parts.factoryCode()))
                .filter(source -> matches(source.getAreaCode(), parts.areaCode()))
                .filter(source -> matches(source.getLineCode(), parts.lineCode()))
                .filter(source -> matches(source.getAssetType(), parts.assetType()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "OperationData source 설정을 찾을 수 없습니다. assetCode=" + parts.assetCode()
                ));
    }

    private void validateSource(OperationDataProperties.Source source) {
        if (source.getSchemaName() != null && !source.getSchemaName().isBlank()) {
            validateIdentifier(source.getSchemaName(), "schemaName");
        }
        validateIdentifier(source.getTableName(), "tableName");
        validateIdentifier(source.getAssetCodeColumn(), "assetCodeColumn");
        validateIdentifier(source.getTypeIndexColumn(), "typeIndexColumn");
        validateIdentifier(source.getValueColumn(), "valueColumn");

        Set<Integer> seenTypeIndexes = source.getMetrics().stream()
                .map(OperationDataProperties.Metric::getTypeIndex)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (seenTypeIndexes.size() != source.getMetrics().size()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "OperationData metric type-index 설정이 중복되었거나 비어 있습니다. tableName=" + source.getTableName()
            );
        }
    }

    private void validateIdentifier(String value, String fieldName) {
        if (value == null || !SQL_IDENTIFIER.matcher(value).matches()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "OperationData " + fieldName + " 설정이 올바르지 않습니다. value=" + value
            );
        }
    }

    private boolean matches(String expected, String actual) {
        return expected != null && actual != null && expected.equalsIgnoreCase(actual);
    }

    private String firstNotBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
