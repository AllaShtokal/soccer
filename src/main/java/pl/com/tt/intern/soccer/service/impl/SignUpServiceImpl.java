package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.mail.MailSender;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserInfo;
import pl.com.tt.intern.soccer.payload.request.SignUpRequest;
import pl.com.tt.intern.soccer.payload.response.SuccessfulSignUpResponse;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.RoleService;
import pl.com.tt.intern.soccer.service.SignUpService;
import pl.com.tt.intern.soccer.service.UserService;
import pl.com.tt.intern.soccer.util.files.FileToString;

import java.io.IOException;

import static java.util.Collections.singleton;
import static pl.com.tt.intern.soccer.model.enums.RoleType.ROLE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    @Value("${docs.path.mail.active}")
    private String fileActiveMailMsg;

    @Value("${account.confirm.link}")
    private String activationLink;

    @Value("${account.confirm.mail.subject}")
    private String subjectActivationLink;

    private final UserService userService;
    private final RoleService roleService;
    private final ConfirmationKeyService confirmationKeyService;
    private final MailSender mailSender;
    private final ModelMapper mapper;

    @Override
    public SuccessfulSignUpResponse signUp(SignUpRequest request) throws Exception {
        if (doPasswordsMatch(request)) {
            UserInfo userInfo = mapper.map(request, UserInfo.class);
            User user = mapper.map(request, User.class);

            userInfo.setUser(user);
            user.setUserInfo(userInfo);
            user.setRoles(singleton(roleService.findByType(ROLE_USER)));

            User result = userService.save(user);
            sendActiveTokenMailMsg(result);
            return new SuccessfulSignUpResponse("User registered successfully", result);
        } else
            throw new PasswordsMismatchException();
    }

    private void sendActiveTokenMailMsg(User user) {
        ConfirmationKey confirmationKey = new ConfirmationKey(user);
        confirmationKeyService.save(confirmationKey);

        try {
            String msg = FileToString.readFileToString(fileActiveMailMsg);
            String msgMail = insertActivationLinkToMailMsg(msg, confirmationKey);

            mailSender.sendSimpleMessageHtml(
                    user.getEmail(),
                    subjectActivationLink,
                    msgMail
            );
        } catch (IOException e) {
            log.error("Throwing an IOException while reading the file.. ", e);
        }
    }

    private boolean doPasswordsMatch(SignUpRequest request) {
        return request.getPassword().equals(request.getConfirmPassword());
    }

    private String insertActivationLinkToMailMsg(String msg, ConfirmationKey confirmationKey) {
        StringBuilder newString = new StringBuilder(msg);

        return newString.insert(
                msg.indexOf("\">Link aktywacyjny</a>"),
                activationLink + confirmationKey.getUuid()
        ).toString();
    }
}
