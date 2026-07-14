package org.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "operation-data")
public class OperationDataProperties {

    private List<Source> sources = new ArrayList<>();

    @Getter
    @Setter
    public static class Source {

        private String factoryCode;
        private String areaCode;
        private String lineCode;
        private String assetType;
        private String tableName;
        private String assetCodeColumn;
        private String typeIndexColumn;
        private String valueColumn;
        private List<Metric> metrics = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class Metric {

        private Integer typeIndex;
        private String idShort;
        private String unit;
        private String valueType = "xs:double";
        private int sortOrder;
    }
}
