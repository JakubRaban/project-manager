package pl.edu.agh.gastronomiastosowana.model.mail;

import pl.edu.agh.gastronomiastosowana.model.mail.MailMessage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMailService {
    static private final String host = "poczta.interia.pl";
    static private final String user = "gastrofaza.agh@interia.pl";
    static private final String password = "GastroFaza123";



    public void sendEmail(MailMessage message) {
        Session session = getSession();

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(user));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(message.getReceiver()));
            mimeMessage.setSubject(message.getSubject());
            mimeMessage.setText(message.getText());
            Transport.send(mimeMessage);
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private Session getSession() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        return Session.getDefaultInstance(properties,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });
    }
}
