package mail.services;

public interface MailSender {

    void sendEmail(String toEmail, String subject, String body);
    
}
