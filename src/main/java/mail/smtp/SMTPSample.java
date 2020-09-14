package mail.smtp;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class SMTPSample {

    public static void main(String[] args) {

        System.out.println("SimpleEmail Start");

        final String SERVER_HOST = "localhost";
        final int SERVER_PORT = 1025;
        final String FROM = "sender@example.com";
        final String FROM_NAME = "Sender Name";

        final String TO = "recipient@example.com";

        final String subject = "SimpleEmail Testing Subject";
        final String body = "SimpleEmail Testing Body";

        final Properties props = System.getProperties();

        props.put("mail.smtp.host", SERVER_HOST);
        props.put("mail.smtp.port", SERVER_PORT);

        //props.put("mail.smtp.auth", "true"); //enable authentication
        //props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        final Session session = Session.getInstance(props);
        // timeout milliseconds, waiting for 6 secs
        props.put("mail.smtp.timeout", 6000);
        // timeout milliseconds, waiting for 6 secs
        props.put("mail.smtp.connectiontimeout", 6000);

        final MimeMessage msg = new MimeMessage(session);

        try {

            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(FROM, FROM_NAME));

            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TO, false));
            System.out.println("Sending message");
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
