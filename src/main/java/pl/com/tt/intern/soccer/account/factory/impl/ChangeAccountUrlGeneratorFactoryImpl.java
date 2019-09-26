package pl.com.tt.intern.soccer.account.factory.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountUrlGeneratorFactory;
import pl.com.tt.intern.soccer.account.url.AccountChangeUrlGenerator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeAccountUrlGeneratorFactoryImpl implements ChangeAccountUrlGeneratorFactory {
    private final List<AccountChangeUrlGenerator> urlGenerators;

    @Override
    public AccountChangeUrlGenerator getUrlGenerator(AccountChangeType type) {
        return urlGenerators.stream()
                .filter(customizer -> customizer.supports(type))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalStateException("Cannot find ChangeAccountUrlGenerator for type: " + type));
    }
}
