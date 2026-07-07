package org.payment.service.aas;

import java.util.List;

public record AasAssetIdParts(
        String factoryCode,
        String areaCode,
        String lineCode,
        String assetType,
        String assetCode,
        boolean matched,
        List<String> warnings
) {
}
