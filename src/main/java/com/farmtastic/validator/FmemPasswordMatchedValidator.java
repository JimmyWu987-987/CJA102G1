
package com.farmtastic.validator;

import com.farmtastic.fmember.model.Fmem;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FmemPasswordMatchedValidator implements ConstraintValidator<FmemPasswordMatches, Fmem> {
	
	@Override
	public boolean isValid(Fmem fmem, ConstraintValidatorContext context) {
		if(fmem.getFmemPwd() == null || fmem.getFmemPwdCheck() == null) {
			return false;
		}
		
		boolean isMatched = fmem.getFmemPwd().equals(fmem.getFmemPwdCheck());
		
		if(!isMatched) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("與第一次輸入的密碼不一致")
					.addPropertyNode("fmemPwdCheck")
					.addConstraintViolation();
		}
		
		
		return isMatched;
	}
}
