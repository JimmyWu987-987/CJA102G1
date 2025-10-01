package com.farmtastic.validator;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinAgeValidator implements ConstraintValidator<MinAge, Date> {
	private int minAge;
	
	@Override
	public void initialize(MinAge constraintAnnotation) {
		this.minAge = constraintAnnotation.value();
	}
	
	@Override
	public boolean isValid(Date birthDate, ConstraintValidatorContext context) {
		if(birthDate == null) return true; // 交給 @NotNull 驗證處理是否為空 
		
		LocalDate birthLocalDate = birthDate.toLocalDate();
		LocalDate today = LocalDate.now();
		return Period.between(birthLocalDate, today).getYears() >= minAge;
	}
}