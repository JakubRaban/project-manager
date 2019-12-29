package pl.edu.agh.gastronomiastosowana.service;

import pl.edu.agh.gastronomiastosowana.model.mail.MailMessage;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMailService {
    private String senderEmail;

    public SendMailService(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void sendEmail(MailMessage message) {
        Session session = getSession();

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(senderEmail));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(message.getReceiver()));
            mimeMessage.setSubject(message.getSubject());
            mimeMessage.setText(message.getText());
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private Session getSession() {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "localhost");
        return Session.getDefaultInstance(properties);
    }
}
