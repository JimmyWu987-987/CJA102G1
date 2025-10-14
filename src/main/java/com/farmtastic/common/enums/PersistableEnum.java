package com.farmtastic.common.enums;

import java.util.Arrays;

//規範所有 Enum
public interface PersistableEnum<T> {
	T getCode();

	static <E extends Enum<E> & PersistableEnum<T>, T> E fromCode(Class<E> enumClass, T code) {
		if (code == null)
			return null;
		return Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.getCode().equals(code)).findFirst()
				.orElseThrow(
						() -> new IllegalArgumentException("未知的 Enum 值: " + code + " in " + enumClass.getSimpleName()));
	}
}
