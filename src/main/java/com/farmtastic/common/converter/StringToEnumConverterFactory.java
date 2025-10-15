package com.farmtastic.common.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

//.....
/**
 * 通用 String → Enum 轉換器工廠。 解決表單傳入字串（例如 "PERCENTAGE"）時，能自動轉換成對應 Enum。
 */
@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

	@Override
	public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
		return new StringToEnumConverter<>(targetType);
	}

	private static class StringToEnumConverter<T extends Enum<?>> implements Converter<String, T> {

		private final Class<T> enumType;

		public StringToEnumConverter(Class<T> enumType) {
			this.enumType = enumType;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public T convert(String source) {
			if (source == null || source.isBlank()) {
				return null;
			}
			try {
				return (T) Enum.valueOf((Class) enumType, source.trim().toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("無效的 Enum 值: " + source + " for Enum " + enumType.getSimpleName());
			}
		}
	}
}