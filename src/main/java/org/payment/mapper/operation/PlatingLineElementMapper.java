package org.payment.mapper.operation;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.payment.domain.operation.OperationMetricRow;

import java.util.List;

@Mapper
public interface PlatingLineElementMapper {

    List<OperationMetricRow> findCurrentValuesByAssetCode(
            @Param("tableName") String tableName,
            @Param("assetCodeColumn") String assetCodeColumn,
            @Param("typeIndexColumn") String typeIndexColumn,
            @Param("valueColumn") String valueColumn,
            @Param("assetCode") String assetCode,
            @Param("typeIndexes") List<Integer> typeIndexes
    );
}
