package org.payment.mapper.operation;

import org.apache.ibatis.annotations.Mapper;
import org.payment.domain.operation.PlatingLineElementRow;

import java.util.List;

@Mapper
public interface PlatingLineElementMapper {

    List<PlatingLineElementRow> findCurrentValuesByAssetCode(String assetCode);
}
