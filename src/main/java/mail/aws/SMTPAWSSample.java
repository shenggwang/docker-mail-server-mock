/*
 * The copyright of this file belongs to Feedzai. The file cannot be
 * reproduced in whole or in part, stored in a retrieval system,
 * transmitted in any form, or by any means electronic, mechanical,
 * photocopying, or otherwise, without the prior permission of the owner.
 *
 * (c) 2020 Feedzai, Strictly Confidential
 */

package mail.aws;

import mail.aws.credentials.DummyData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Test with SMTP server
 */
public class SMTPAWSSample {

    private enum Credentials {
        ACCESS_KEY("aws_access_key_id"),
        SECRET_ACCESS_KEY("aws_secret_access_key");

        private final String value;

        Credentials(String value) {
            this.value = value;
        }

        public String value() { return value; }
    }

    private static String getCredentials(final Credentials credentialsType) {

        final String path = System.getProperty("user.home")+"/.aws/credentials";
        final File credentialsFile = new File(path);

        String value = null;
        System.out.println(credentialsType.value());
        try (final Scanner myReader = new Scanner(credentialsFile)) {
            while (myReader.hasNextLine()) {
                final String data = myReader.nextLine();
                if (data.contains(credentialsType.value())) {
                    value = data.split("=")[1].trim();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static final String BODY = String.join(
            System.getProperty("line.separator"),
            "<h1>Amazon SES SMTP Email Test</h1>",
            "<p>This email was sent with Amazon SES using the ",
            "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
            " for <a href='https://www.java.com'>Java</a>."
    );

    public static void main(String[] args) throws Exception {

        // Create a Properties object to contain connection configuration information.
        final Properties props = System.getProperties();
        props.put("mail.transport.protocol", "aws");
        /*
        props.put("mail.smtp.port", DummyData.PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        // timeout milliseconds, waiting for 6 secs
        props.put("mail.smtp.timeout", 6000);
        // timeout milliseconds, waiting for 6 secs
        props.put("mail.smtp.connectiontimeout", 6000);
        // below are not mandatory to make this working.
        props.put("mail.aws.region", "us-east-1");
        props.put("mail.aws.host", "localhost");
        props.put("mail.aws.user", "user");         //SMTPAWSSample.getCredentials(Credentials.ACCESS_KEY)
        props.put("mail.aws.password", "password"); //SMTPAWSSample.getCredentials(Credentials.SECRET_ACCESS_KEY)
        */

        // Create a Session object to represent a mail session with the specified properties. 
        final Session session = Session.getInstance(props);

        // Create a message with the specified information. 
        final MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(DummyData.FROM,DummyData.FROM_NAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(DummyData.TO));
        msg.setSubject(DummyData.SUBJECT);
        msg.setContent(SMTPAWSSample.BODY,"text/html");

        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set
        msg.setHeader("X-SES-CONFIGURATION-SET", DummyData.CONFIGSET);

        // Create a transport.
        try (final Transport transport = session.getTransport()) {
            System.out.println("Sending...");

            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(DummyData.HOST, 9001, DummyData.SMTP_USERNAME, DummyData.SMTP_PASSWORD);

            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        } catch (final Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
            ex.printStackTrace();
        }
        // Close and terminate the connection.
        System.exit(0);
    }
}
