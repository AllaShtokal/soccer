package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.User;

public interface SendMailService {
    void sendEmailWithMessageFromFileAndInsertLinkWithToken(User user, String fileName, String subject, String linkToInsert, String indexOfByText);
}
