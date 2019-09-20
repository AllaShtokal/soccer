package pl.com.tt.intern.soccer.exception.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationService {

    public static Map<String, String> validate(BindingResult result) {
        Map<String, String> validationMap = new HashMap<>();

        result.getFieldErrors().stream()
                .forEach(error -> validationMap.put(error.getField(), error.getDefaultMessage()));

        return validationMap;
    }

}
