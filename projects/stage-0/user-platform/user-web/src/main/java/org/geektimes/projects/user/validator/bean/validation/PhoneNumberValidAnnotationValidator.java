package org.geektimes.projects.user.validator.bean.validation;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidAnnotationValidator implements ConstraintValidator<PhoneNumberValid, String> {
    final Pattern pattern = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$", Pattern.CASE_INSENSITIVE);

    public void initialize(PhoneNumberValid annotation) {
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return !StringUtils.isBlank(phone) && pattern.matcher(phone).matches();
    }
}
