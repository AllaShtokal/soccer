package pl.com.tt.intern.soccer.mail.customizer;

public interface CustomizerSender {
    void insertLinkToMsgAndSendMail(String emailTo, String fileName, String subject, String linkToInsert);
}
