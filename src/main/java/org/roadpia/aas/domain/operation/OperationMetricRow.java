package org.roadpia.aas.domain.operation;

import java.math.BigDecimal;

public class OperationMetricRow {

    private int typeIndex;
    private BigDecimal value;

    public int getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(int typeIndex) {
        this.typeIndex = typeIndex;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
