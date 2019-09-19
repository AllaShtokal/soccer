package pl.com.tt.intern.soccer.mail.customizer;

public interface CustomizedSender {
    void insertLinkToMsgAndSendMail(String emailTo, String fileName, String subject, String linkToInsert);
}
