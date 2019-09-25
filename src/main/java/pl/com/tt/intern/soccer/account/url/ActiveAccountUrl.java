package pl.com.tt.intern.soccer.account.url;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.url.util.HostGeneratorUtil;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.ACTIVE_ACCOUNT;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActiveAccountUrl implements ChangeAccountUrlGenerator {

    private final String URL_SUFFIX = "login";
    private final String ACTIVE_KEY_PARAM = "activeKey";
    private final ConfirmationKeyService confirmationKeyService;

    @SneakyThrows
    @Override
    public String generate(String email, String... params) {
        String uuid = confirmationKeyService.createAndAssignToUserByEmail(email).getUuid();
        return createUrl(uuid);
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return ACTIVE_ACCOUNT.equals(type);
    }

    private String createUrl(String uuid) {
        return UriComponentsBuilder.newInstance()
                .host(HostGeneratorUtil.generate())
                .path(URL_SUFFIX)
                .queryParam(ACTIVE_KEY_PARAM, uuid)
                .build()
                .toString();
    }
}
