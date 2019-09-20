package pl.com.tt.intern.soccer.annotation.validator;

import org.passay.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.annotation.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Service
public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {

    @Bean
    public PasswordValidator passwordValidator() {
        return new PasswordValidator(asList(
                new LengthRule(8, 20),
                new UppercaseCharacterRule(1),
                new LowercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new WhitespaceRule()
        ));
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = passwordValidator();

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid())
            return true;

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                validator.getMessages(result).stream().collect(Collectors.joining(" ")))
                .addConstraintViolation();

        return false;
    }

}
