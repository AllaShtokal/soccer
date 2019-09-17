package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.ConfirmationKey;

public interface SendMailService {
    void sendEmailWithMessageFromFileAndInsertLinkWithToken(ConfirmationKey confirmationKey, String fileName, String subject, String linkToInsert, String indexOfByText);
}
