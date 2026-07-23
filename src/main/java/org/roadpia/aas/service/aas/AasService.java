package org.roadpia.aas.service.aas;

import com.fasterxml.jackson.databind.JsonNode;
import org.roadpia.aas.client.BasyxClient;
import org.roadpia.aas.domain.aas.AasAssetDetailResponse;
import org.roadpia.aas.domain.aas.AasAssetResponse;
import org.roadpia.aas.domain.aas.AasAssetSummaryResponse;
import org.roadpia.aas.domain.aas.AasAssetSubmodelsResponse;
import org.roadpia.aas.domain.aas.AasAssetTreeResponse;
import org.roadpia.aas.domain.aas.AasWarningResponse;
import org.roadpia.aas.domain.aas.AreaNodeResponse;
import org.roadpia.aas.domain.aas.AssetNodeResponse;
import org.roadpia.aas.domain.aas.AssetTypeNodeResponse;
import org.roadpia.aas.domain.aas.FactoryNodeResponse;
import org.roadpia.aas.domain.aas.LineNodeResponse;
import org.roadpia.aas.domain.aas.SubmodelResponse;
import org.roadpia.aas.service.operation.OperationDataService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class AasService {

    private static final String NAMEPLATE = "nameplate";
    private static final String TECHNICAL_DATA = "technicalData";

    private final BasyxClient basyxClient;
    private final AasAssetIdParser aasAssetIdParser;
    private final OperationDataService operationDataService;

    public AasService(
            BasyxClient basyxClient,
            AasAssetIdParser aasAssetIdParser,
            OperationDataService operationDataService
    ) {
        this.basyxClient = basyxClient;
        this.aasAssetIdParser = aasAssetIdParser;
        this.operationDataService = operationDataService;
    }

    public List<AasAssetSummaryResponse> getAssets() {
        return listShells().stream()
                .map(this::toAssetSummary)
                .sorted(Comparator.comparing(AasAssetSummaryResponse::assetCode, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public AasAssetTreeResponse getAssetTree() {
        List<AasAssetSummaryResponse> assets = getAssets();
        Map<String, MutableFactoryNode> factories = new LinkedHashMap<>();
        List<AasWarningResponse> warnings = new ArrayList<>();

        for (AasAssetSummaryResponse asset : assets) {
            warnings.addAll(asset.warnings());

            MutableFactoryNode factory = factories.computeIfAbsent(
                    asset.factoryCode(),
                    code -> new MutableFactoryNode(code, code)
            );
            MutableAreaNode area = factory.areas.computeIfAbsent(
                    asset.areaCode(),
                    code -> new MutableAreaNode(code, code)
            );
            MutableLineNode line = area.lines.computeIfAbsent(
                    asset.lineCode(),
                    code -> new MutableLineNode(code, code)
            );
            MutableAssetTypeNode assetType = line.assetTypes.computeIfAbsent(
                    asset.assetType(),
                    code -> new MutableAssetTypeNode(code, code)
            );
            assetType.assets.add(new AssetNodeResponse(
                    asset.aasId(),
                    asset.idShort(),
                    asset.assetCode(),
                    asset.name(),
                    asset.warnings()
            ));
        }

        return new AasAssetTreeResponse(
                factories.values().stream().map(MutableFactoryNode::toResponse).toList(),
                warnings
        );
    }

    public AasAssetDetailResponse getAsset(String assetCode) {
        JsonNode shell = findShellByAssetCode(assetCode);
        AasAssetSummaryResponse asset = toAssetSummary(shell);
        return new AasAssetDetailResponse(
                toAssetResponse(
                        asset,
                        new AasAssetSubmodelsResponse(
                                getSubmodel(shell, asset.assetCode(), NAMEPLATE),
                                getSubmodel(shell, asset.assetCode(), TECHNICAL_DATA),
                                operationDataService.getCurrentOperationData(asset.assetCode())
                        )
                )
        );
    }

    public AasAssetDetailResponse getNameplate(String assetCode) {
        JsonNode shell = findShellByAssetCode(assetCode);
        AasAssetSummaryResponse asset = toAssetSummary(shell);
        return new AasAssetDetailResponse(
                toAssetResponse(
                        asset,
                        new AasAssetSubmodelsResponse(
                                getSubmodel(shell, asset.assetCode(), NAMEPLATE),
                                null,
                                null
                        )
                )
        );
    }

    public AasAssetDetailResponse getTechnicalData(String assetCode) {
        JsonNode shell = findShellByAssetCode(assetCode);
        AasAssetSummaryResponse asset = toAssetSummary(shell);
        return new AasAssetDetailResponse(
                toAssetResponse(
                        asset,
                        new AasAssetSubmodelsResponse(
                                null,
                                getSubmodel(shell, asset.assetCode(), TECHNICAL_DATA),
                                null
                        )
                )
        );
    }

    private AasAssetResponse toAssetResponse(
            AasAssetSummaryResponse asset,
            AasAssetSubmodelsResponse submodels
    ) {
        return new AasAssetResponse(
                asset.aasId(),
                asset.idShort(),
                asset.factoryCode(),
                asset.areaCode(),
                asset.lineCode(),
                asset.assetType(),
                asset.assetCode(),
                asset.name(),
                asset.warnings(),
                submodels
        );
    }

    private JsonNode findShellByAssetCode(String assetCode) {
        return listShells().stream()
                .filter(shell -> {
                    AasAssetIdParts parts = parseShellId(shell);
                    return parts.assetCode().equalsIgnoreCase(assetCode);
                })
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "자산을 찾을 수 없습니다. assetCode=" + assetCode
                ));
    }

    private AasAssetSummaryResponse toAssetSummary(JsonNode shell) {
        AasAssetIdParts parts = parseShellId(shell);
        List<AasWarningResponse> warnings = parts.warnings().stream()
                .map(message -> new AasWarningResponse("INVALID_AAS_ID", message))
                .toList();

        String name = firstNotBlank(
                extractDisplayName(shell),
                resolveSubmodelDisplayName(shell, parts.assetCode(), NAMEPLATE),
                resolveSubmodelDisplayName(shell, parts.assetCode(), "identification"),
                parts.assetCode()
        );

        return new AasAssetSummaryResponse(
                text(shell, "id"),
                text(shell, "idShort"),
                parts.factoryCode(),
                parts.areaCode(),
                parts.lineCode(),
                parts.assetType(),
                parts.assetCode(),
                name,
                warnings
        );
    }

    private AasAssetIdParts parseShellId(JsonNode shell) {
        return aasAssetIdParser.parse(text(shell, "id"), text(shell, "idShort"));
    }

    private SubmodelResponse getSubmodel(JsonNode shell, String assetCode, String idShort) {
        return findSubmodelJsonByIdShort(shell, idShort)
                .map(submodel -> toSubmodelResponse(submodel, assetCode, idShort))
                .orElse(null);
    }

    private Optional<JsonNode> findSubmodelJsonByIdShort(JsonNode shell, String desiredIdShort) {
        JsonNode submodelRefs = shell.path("submodels");
        if (!submodelRefs.isArray()) {
            return Optional.empty();
        }

        for (JsonNode submodelRef : submodelRefs) {
            String submodelId = extractReferencedId(submodelRef);
            if (submodelId == null || submodelId.isBlank()) {
                continue;
            }

            try {
                JsonNode submodel = basyxClient.getSubmodel(submodelId);
                if (matchesSubmodel(submodel, desiredIdShort)) {
                    return Optional.of(submodel);
                }
            } catch (RuntimeException ignored) {
                // Submodel 조회 실패가 전체 자산 목록 API 실패로 이어지지 않도록 무시한다.
            }
        }

        return Optional.empty();
    }

    private boolean matchesSubmodel(JsonNode submodel, String desiredId) {
        String idShort = text(submodel, "idShort");
        if (desiredId.equalsIgnoreCase(idShort)) {
            return true;
        }

        String id = text(submodel, "id");
        return id != null && id.toLowerCase().endsWith(":" + desiredId.toLowerCase());
    }

    private String resolveSubmodelDisplayName(JsonNode shell, String assetCode, String idShort) {
        return findSubmodelJsonByIdShort(shell, idShort)
                .map(submodel -> toSubmodelResponse(submodel, assetCode, idShort))
                .map(SubmodelResponse::values)
                .map(this::findDisplayValue)
                .orElse(null);
    }

    private SubmodelResponse toSubmodelResponse(JsonNode submodel, String assetCode, String idShort) {
        Map<String, Object> values = new LinkedHashMap<>();
        collectElementValues(submodel.path("submodelElements"), values);

        return new SubmodelResponse(
                assetCode + ":" + idShort,
                idShort,
                firstNotBlank(extractDisplayName(submodel), findDisplayValue(values), idShort),
                values,
                submodel
        );
    }

    private List<JsonNode> listShells() {
        JsonNode shells = basyxClient.getShells();
        JsonNode result = shells.isArray() ? shells : shells.path("result");
        if (!result.isArray()) {
            return List.of();
        }

        return StreamSupport.stream(result.spliterator(), false)
                .filter(Objects::nonNull)
                .toList();
    }

    private String extractReferencedId(JsonNode reference) {
        JsonNode keys = reference.path("keys");
        if (keys.isArray() && !keys.isEmpty()) {
            JsonNode lastKey = keys.get(keys.size() - 1);
            return text(lastKey, "value");
        }

        return text(reference, "value");
    }

    private void collectElementValues(JsonNode elements, Map<String, Object> values) {
        if (!elements.isArray()) {
            return;
        }

        for (JsonNode element : elements) {
            String key = text(element, "idShort");
            JsonNode value = element.path("value");

            if (key == null || key.isBlank()) {
                continue;
            }

            if (value.isArray()) {
                Map<String, Object> nestedValues = new LinkedHashMap<>();
                collectElementValues(value, nestedValues);
                values.put(key, nestedValues);
            } else if (!value.isMissingNode() && !value.isNull()) {
                values.put(key, jsonToObject(value));
            }
        }
    }

    private Object jsonToObject(JsonNode node) {
        if (node.isTextual()) {
            return node.asText();
        }
        if (node.isInt() || node.isLong()) {
            return node.asLong();
        }
        if (node.isFloat() || node.isDouble() || node.isBigDecimal()) {
            return node.asDouble();
        }
        if (node.isBoolean()) {
            return node.asBoolean();
        }
        if (node.isArray()) {
            List<Object> values = new ArrayList<>();
            node.forEach(item -> values.add(jsonToObject(item)));
            return values;
        }
        if (node.isObject()) {
            Map<String, Object> values = new LinkedHashMap<>();
            node.fields().forEachRemaining(entry -> values.put(entry.getKey(), jsonToObject(entry.getValue())));
            return values;
        }
        return node.asText();
    }

    @SuppressWarnings("unchecked")
    private String findDisplayValue(Map<String, Object> values) {
        List<String> candidateKeys = List.of(
                "displayName",
                "assetName",
                "assetDisplayName",
                "name",
                "manufacturerProductDesignation",
                "ManufacturerProductDesignation"
        );

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (candidateKeys.stream().anyMatch(candidate -> candidate.equalsIgnoreCase(entry.getKey()))
                    && entry.getValue() != null
                    && !entry.getValue().toString().isBlank()) {
                return entry.getValue().toString();
            }

            if (entry.getValue() instanceof Map<?, ?> nested) {
                String nestedValue = findDisplayValue((Map<String, Object>) nested);
                if (nestedValue != null) {
                    return nestedValue;
                }
            }
        }

        return null;
    }

    private String extractDisplayName(JsonNode node) {
        JsonNode displayName = node.path("displayName");
        if (displayName.isTextual()) {
            return displayName.asText();
        }
        if (displayName.isArray()) {
            for (JsonNode name : displayName) {
                String text = text(name, "text");
                if (text != null && !text.isBlank()) {
                    return text;
                }
            }
        }
        return null;
    }

    private String text(JsonNode node, String fieldName) {
        JsonNode value = node.path(fieldName);
        return value.isTextual() ? value.asText() : null;
    }

    private String firstNotBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private static class MutableFactoryNode {
        private final String code;
        private final String name;
        private final Map<String, MutableAreaNode> areas = new LinkedHashMap<>();

        private MutableFactoryNode(String code, String name) {
            this.code = code;
            this.name = name;
        }

        private FactoryNodeResponse toResponse() {
            return new FactoryNodeResponse(
                    code,
                    name,
                    areas.values().stream().map(MutableAreaNode::toResponse).toList()
            );
        }
    }

    private static class MutableAreaNode {
        private final String code;
        private final String name;
        private final Map<String, MutableLineNode> lines = new LinkedHashMap<>();

        private MutableAreaNode(String code, String name) {
            this.code = code;
            this.name = name;
        }

        private AreaNodeResponse toResponse() {
            return new AreaNodeResponse(
                    code,
                    name,
                    lines.values().stream().map(MutableLineNode::toResponse).toList()
            );
        }
    }

    private static class MutableLineNode {
        private final String code;
        private final String name;
        private final Map<String, MutableAssetTypeNode> assetTypes = new LinkedHashMap<>();

        private MutableLineNode(String code, String name) {
            this.code = code;
            this.name = name;
        }

        private LineNodeResponse toResponse() {
            return new LineNodeResponse(
                    code,
                    name,
                    assetTypes.values().stream().map(MutableAssetTypeNode::toResponse).toList()
            );
        }
    }

    private static class MutableAssetTypeNode {
        private final String code;
        private final String name;
        private final List<AssetNodeResponse> assets = new ArrayList<>();

        private MutableAssetTypeNode(String code, String name) {
            this.code = code;
            this.name = name;
        }

        private AssetTypeNodeResponse toResponse() {
            return new AssetTypeNodeResponse(code, name, assets);
        }
    }
}
