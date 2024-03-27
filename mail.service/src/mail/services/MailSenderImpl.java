package mail.services;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSenderImpl implements git MailSender {

	public void start() {
		System.out.println("Mail sender component is starting ...");
	}
	
	private MimeMessage setupMimeMessage(Session session, String toEmail, String subject, String body) throws Exception {
	    MimeMessage mimeMessage = new MimeMessage(session);

	    mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
	    mimeMessage.addHeader("format", "flowed");
	    mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");

	    mimeMessage.setFrom(new InternetAddress(Constants.ORG_MAIL_ADDR));
	    mimeMessage.setReplyTo(InternetAddress.parse(Constants.ORG_MAIL_ADDR, false));

	    mimeMessage.setSubject(subject, "UTF-8");
	    mimeMessage.setText(body, "UTF-8");
	    mimeMessage.setSentDate(new Date());
	    mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

	    return mimeMessage;
	}

	private void sendEmail(Session session, String toEmail, String subject, String body) {
	    try {
	        MimeMessage mimeMessage = setupMimeMessage(session, toEmail, subject, body);
	        Transport.send(mimeMessage);
	        System.out.println("An email has been sent successfully!");
	    } catch (Exception exception) {
	        exception.printStackTrace();
	    }
	}

	private static Properties setupSTMP() {
	    Properties properties = new Properties();

	    properties.put("mail.smtp.host", Constants.SMTP_HOST);
	    properties.put("mail.smtp.port", Constants.STMP_PORT);
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.starttls.enable", "true");

	    return properties;
	}

	private Session setupSession() {
	    Properties properties = setupSTMP();

	    Authenticator authenticator = new Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(Constants.ORG_MAIL_ADDR, Constants.ORG_MAIL_PASS);
	        }
	    };

	    return Session.getInstance(properties, authenticator);
	}

	public void sendEmail(String toEmail, String subject, String body) {
	    Session session = setupSession();
	    sendEmail(session, toEmail, subject, body);
	}
	
	public void stop() {
		System.out.println("Mail sender component is stopping ...");
	}
	
}
