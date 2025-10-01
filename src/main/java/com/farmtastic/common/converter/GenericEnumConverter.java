package com.farmtastic.common.converter;

import com.farmtastic.common.enums.PersistableEnum;

import jakarta.persistence.AttributeConverter;

//通用的 JPA Enum <-> DB 值 轉換器。
//E 必須是一個 Enum&還要實作我們自訂的介面
public abstract class GenericEnumConverter<E extends Enum<E> & PersistableEnum<T>, T>
		implements AttributeConverter<E, T> {
	// 保存 Enum 的 class 型態資訊，因為JAVA會泛型型別擦除
	private final Class<E> enumClass;

	// 建構子傳入 具體 Enum 類別
	protected GenericEnumConverter(Class<E> enemClass) {
		this.enumClass = enemClass;
	}

	// 把 Enum -> 存進資料庫的值
	@Override
	public T convertToDatabaseColumn(E attribute) {
		// 如果傳進來的 attribute 不是 null ?true 執行getCode() :false 回傳null
		return attribute != null ? attribute.getCode() : null;
	}

	// 把資料庫的值 -> Enum
	@Override
	public E convertToEntityAttribute(T dbData) {
		if (dbData == null)
			return null;
		else
			for (E e : enumClass.getEnumConstants()) {
				// 每個 Enum 都有一個 code，比對資料庫 dbData(0/1/2)
				if (e.getCode().equals(dbData))
					return e;
			}
		throw new IllegalArgumentException("未設定代碼: " + dbData + " for enum " + enumClass.getSimpleName());
	}

}
