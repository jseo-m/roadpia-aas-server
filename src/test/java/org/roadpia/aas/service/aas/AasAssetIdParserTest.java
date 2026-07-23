package org.roadpia.aas.service.aas;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AasAssetIdParserTest {

    private final AasAssetIdParser parser = new AasAssetIdParser();

    @Test
    void parsesCompactAssetIdFromIdShort() {
        AasAssetIdParts parts = parser.parse(null, "MTK01_F1_NI1_REC01");

        assertThat(parts.matched()).isTrue();
        assertThat(parts.factoryCode()).isEqualTo("MTK01");
        assertThat(parts.areaCode()).isEqualTo("F1");
        assertThat(parts.lineCode()).isEqualTo("NI1");
        assertThat(parts.assetType()).isEqualTo("REC");
        assertThat(parts.assetCode()).isEqualTo("MTK01_F1_NI1_REC01");
        assertThat(parts.warnings()).isEmpty();
    }

    @Test
    void parsesCompactAssetIdFromUrnSuffix() {
        AasAssetIdParts parts = parser.parse("urn:aas:kr:mtk01:MTK01_F1_NI1_REC01", null);

        assertThat(parts.matched()).isTrue();
        assertThat(parts.factoryCode()).isEqualTo("MTK01");
        assertThat(parts.areaCode()).isEqualTo("F1");
        assertThat(parts.lineCode()).isEqualTo("NI1");
        assertThat(parts.assetType()).isEqualTo("REC");
        assertThat(parts.assetCode()).isEqualTo("MTK01_F1_NI1_REC01");
        assertThat(parts.warnings()).isEmpty();
    }

    @Test
    void rejectsPreviousFiveTokenRule() {
        AasAssetIdParts parts = parser.parse(null, "MTK01_F1_NI1_REC_01");

        assertThat(parts.matched()).isFalse();
        assertThat(parts.assetCode()).isEqualTo("MTK01_F1_NI1_REC_01");
    }
}
