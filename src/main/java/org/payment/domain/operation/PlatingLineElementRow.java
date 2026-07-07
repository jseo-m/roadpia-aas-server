package org.payment.domain.operation;

import java.math.BigDecimal;

public class PlatingLineElementRow {

    private int platingElementTypeIndex;
    private BigDecimal platingNowValue;

    public int getPlatingElementTypeIndex() {
        return platingElementTypeIndex;
    }

    public void setPlatingElementTypeIndex(int platingElementTypeIndex) {
        this.platingElementTypeIndex = platingElementTypeIndex;
    }

    public BigDecimal getPlatingNowValue() {
        return platingNowValue;
    }

    public void setPlatingNowValue(BigDecimal platingNowValue) {
        this.platingNowValue = platingNowValue;
    }
}
