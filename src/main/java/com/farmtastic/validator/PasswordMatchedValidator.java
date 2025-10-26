package com.farmtastic.validator;

import com.farmtastic.member.model.Mem;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchedValidator implements ConstraintValidator<PasswordMatches, Mem> {
	
	@Override
	public boolean isValid(Mem mem, ConstraintValidatorContext context) {
		if(mem.getMemPwd() == null || mem.getMemPwdCheck() == null) {
			return false;
		}
		boolean isMatched = mem.getMemPwd().equals(mem.getMemPwdCheck());
		
		if(!isMatched) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("與第一次輸入的密碼不一致")
					.addPropertyNode("memPwdCheck")
					.addConstraintViolation();
		}
		return isMatched;
	}
}
