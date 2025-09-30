package com.farmtastic.validator;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = PasswordMatchedValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {

	String message() default "密碼與確認密碼不相符";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default{};
}
