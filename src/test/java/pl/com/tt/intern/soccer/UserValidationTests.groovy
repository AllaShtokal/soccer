package pl.com.tt.intern.soccer

import org.passay.PasswordData
import org.passay.PasswordValidator
import org.passay.RuleResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class UserValidationTests extends Specification {

    @Autowired
    PasswordValidator validator

    def "password is correct"() {
        given:
        String password = "Abcd1234"
        when:
        RuleResult result = validator.validate(new PasswordData(password))
        then:
        result.isValid()
    }

    def "password is too short"() {
        given:
        String password = "Abcd"
        when:
        RuleResult result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password is too long"() {
        given:
        String password = "Abcdefghijklmn123456789"
        when:
        RuleResult result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has no 1 upper"() {
        given:
        String password = "abcd1234"
        when:
        RuleResult result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has no 1 digit"() {
        given:
        String password = "Abcdabcd"
        when:
        RuleResult result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has only smalls"() {
        given:
        String password = "abcdabcd"
        when:
        RuleResult result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has only uppers"() {
        given:
        String password = "ABCDABCD"
        when:
        RuleResult result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has only digits"() {
        given:
        String password = "12341234"
        when:
        RuleResult result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has upper, small, but 0 digit"() {
        given:
        String password = "AAAAaaaa"
        when:
        RuleResult result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has upper, digit, but 0 small"() {
        given:
        String password = "AAAA1234"
        when:
        RuleResult result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has digit, small, but 0 upper"() {
        given:
        String password = "aaaa1234"
        when:
        RuleResult result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

}
