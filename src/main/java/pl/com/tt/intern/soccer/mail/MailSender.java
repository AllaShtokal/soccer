package pl.com.tt.intern.soccer.mail;

public interface MailSender {

    void sendSimpleMessageText(String to, String subject, String text);

    void sendSimpleMessageHtml(String to, String subject, String text);

}
