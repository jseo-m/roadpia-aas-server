package org.payment.service.aas;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AasAssetIdParser {

    private static final String UNKNOWN = "unknown";
    private static final Pattern COMPACT_ASSET_TOKEN = Pattern.compile("^([A-Za-z]+)([0-9].*)$");

    public AasAssetIdParts parse(String aasId, String idShort) {
        String normalizedIdShort = normalize(idShort);
        String normalizedAasId = normalize(aasId);

        AasAssetIdParts idShortParts = parseCompactAssetId(normalizedIdShort);
        if (idShortParts.matched()) {
            return idShortParts;
        }

        AasAssetIdParts urnParts = parseCompactAssetId(lastUrnSegment(normalizedAasId));
        if (urnParts.matched()) {
            return urnParts;
        }

        AasAssetIdParts aasIdParts = parseCompactAssetId(normalizedAasId);
        if (aasIdParts.matched()) {
            return aasIdParts;
        }

        List<String> warnings = new ArrayList<>();
        warnings.add("AAS ID 또는 idShort가 공장_구역_라인_자산유형자산코드 규칙과 일치하지 않아 unknown 그룹으로 분류했습니다.");
        String fallbackAssetCode = firstNotBlank(lastPathSegment(normalizedAasId), normalizedIdShort, normalizedAasId, UNKNOWN);
        return new AasAssetIdParts(
                UNKNOWN,
                UNKNOWN,
                UNKNOWN,
                UNKNOWN,
                fallbackAssetCode,
                false,
                warnings
        );
    }

    private AasAssetIdParts parseCompactAssetId(String value) {
        if (value == null || value.isBlank()) {
            return unmatched();
        }

        String[] tokens = value.split("_");
        if (tokens.length != 4) {
            return unmatched();
        }

        String factoryCode = tokens[0];
        String areaCode = tokens[1];
        String lineCode = tokens[2];
        String compactAssetCode = tokens[3];
        Matcher matcher = COMPACT_ASSET_TOKEN.matcher(compactAssetCode);

        if (!matcher.matches()) {
            return unmatched();
        }

        String assetType = matcher.group(1);
        String assetCode = value;
        if (factoryCode.isBlank() || areaCode.isBlank() || lineCode.isBlank() || assetType.isBlank()) {
            return unmatched();
        }

        return new AasAssetIdParts(
                factoryCode,
                areaCode,
                lineCode,
                assetType,
                assetCode,
                true,
                List.of()
        );
    }

    private AasAssetIdParts unmatched() {
        return new AasAssetIdParts(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN, false, List.of());
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String firstNotBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return UNKNOWN;
    }

    private String lastPathSegment(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        int index = value.lastIndexOf('/');
        return index >= 0 && index < value.length() - 1 ? value.substring(index + 1) : value;
    }

    private String lastUrnSegment(String value) {
        if (value == null || value.isBlank() || !value.startsWith("urn:")) {
            return null;
        }
        int index = value.lastIndexOf(':');
        return index >= 0 && index < value.length() - 1 ? value.substring(index + 1) : value;
    }
}
