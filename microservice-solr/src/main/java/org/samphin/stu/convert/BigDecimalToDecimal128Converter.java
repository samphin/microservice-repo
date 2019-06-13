package org.samphin.stu.convert;

import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * 将Decimal类型转换成mongodb支持的数据类型Decimal128
 * @author samphin
 *
 */
public class BigDecimalToDecimal128Converter implements Converter<BigDecimal, Decimal128> {
	 
    @Override
    public Decimal128 convert(BigDecimal source) {
        return new Decimal128(source);
    }
}
