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
        def password = "Abcd1234"
        when:
        RuleResult result = validator.validate(new PasswordData(password))
        then:
        result.isValid()
    }

    def "password is too short"() {
        given:
        def password = "Abcd"
        when:
        def result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password is too long"() {
        given:
        def password = "Abcdefghijklmn123456789"
        when:
        def result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has no 1 lower"() {
        given:
        def password = "ABCD1234"
        when:
        def result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has no 1 upper"() {
        given:
        def password = "abcd1234"
        when:
        def result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has no 1 digit"() {
        given:
        def password = "Abcdabcd"
        when:
        def result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has only lowers"() {
        given:
        def password = "abcdabcd"
        when:
        def result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has only uppers"() {
        given:
        def password = "ABCDABCD"
        when:
        def result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has only digits"() {
        given:
        def password = "12341234"
        when:
        def result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has upper, lower, but 0 digit"() {
        given:
        def password = "ABCDabcd"
        when:
        def result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has upper, digit, but 0 lower"() {
        given:
        def password = "ABCD1234"
        when:
        def result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has digit, lower, but 0 upper"() {
        given:
        def password = "aaaa1234"
        when:
        def result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

    def "password has 10 whitespaces"() {
        given:
        def password = "          "
        when:
        def result = validator.validate(new PasswordData(password))
        then:
        !result.isValid()
    }

}
