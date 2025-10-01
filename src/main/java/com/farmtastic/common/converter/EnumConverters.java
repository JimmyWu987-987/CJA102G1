package com.farmtastic.common.converter;

import com.farmtastic.common.enums.DiscountType;
import com.farmtastic.common.enums.IsActive;

import jakarta.persistence.Converter;

//Enum通用轉換器
public class EnumConverters {
	// JPA Converter implements AttributeConverter<JAVA端用類型, 資料庫欄位的類型>

	@Converter(autoApply = false) // 讓 JPA 不要自動應用這個轉換器
	public static class IsActiveConverter extends GenericEnumConverter<IsActive, Byte> {
		public IsActiveConverter() {
			super(IsActive.class);
		}
	}

	@Converter(autoApply = false)
	public static class DiscountTypeConverter extends GenericEnumConverter<DiscountType, Byte> {
		public DiscountTypeConverter() {
			super(DiscountType.class);
		}
	}
}
