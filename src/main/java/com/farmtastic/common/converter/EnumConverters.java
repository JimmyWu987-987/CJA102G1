package com.farmtastic.common.converter;

import com.farmtastic.common.enums.ApplScope;
import com.farmtastic.common.enums.CpnSource;
import com.farmtastic.common.enums.CpnUseStatus;
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

	@Converter(autoApply = false)
	public static class CpnUseStatusConverter extends GenericEnumConverter<CpnUseStatus, Byte> {
		public CpnUseStatusConverter() {
			super(CpnUseStatus.class);
		}
	}

	@Converter(autoApply = false)
	public static class ApplScopeConverter extends GenericEnumConverter<ApplScope, Byte> {
		public ApplScopeConverter() {
			super(ApplScope.class);
		}
	}

	@Converter(autoApply = false)
	public static class CpnSourceConverter extends GenericEnumConverter<CpnSource, String> {
		public CpnSourceConverter() {
			super(CpnSource.class);
		}
	}
}
