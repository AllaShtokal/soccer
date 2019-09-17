package pl.com.tt.intern.soccer.mail;

import pl.com.tt.intern.soccer.model.ConfirmationKey;

public interface MailCustomizer {
    void sendEmailWithMessageFromFileAndInsertLinkWithToken(ConfirmationKey confirmationKey,
                                                            String fileName,
                                                            String subject,
                                                            String linkToInsert,
                                                            String indexOfByText);
}
