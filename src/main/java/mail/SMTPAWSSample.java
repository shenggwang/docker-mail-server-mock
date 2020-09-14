/*
 * The copyright of this file belongs to Feedzai. The file cannot be
 * reproduced in whole or in part, stored in a retrieval system,
 * transmitted in any form, or by any means electronic, mechanical,
 * photocopying, or otherwise, without the prior permission of the owner.
 *
 * (c) 2020 Feedzai, Strictly Confidential
 */

package mail;

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

    // Replace sender@example.com with your "From" address.
    // This address must be verified.
    static final String FROM = "sender@example.com";
    static final String FROMNAME = "Sender Name";

    // Replace recipient@example.com with a "To" address. If your account 
    // is still in the sandbox, this address must be verified.
    static final String TO = "recipient@example.com";

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


    // Replace smtp_username with your Amazon SES SMTP user name.
    static final String SMTP_USERNAME = "smtp_user";

    // Replace smtp_password with your Amazon SES SMTP password.
    static final String SMTP_PASSWORD = "smtp_pass";

    // The name of the Configuration Set to use for this message.
    // If you comment out or remove this variable, you will also need to
    // comment out or remove the header below.
    static final String CONFIGSET = "ConfigSet";

    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    // See https://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
    // for more information.
    static final String HOST = "http://localhost:9001";

    // The port you will connect to on the Amazon SES SMTP endpoint. 
    static final int PORT = 9001;

    static final String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";

    static final String BODY = String.join(
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
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        // timeout milliseconds, waiting for 6 secs
        props.put("mail.smtp.timeout", 6000);
        // timeout milliseconds, waiting for 6 secs
        props.put("mail.smtp.connectiontimeout", 6000);
        // below are not mandatory to make this working.
        props.put("mail.aws.region", "us-east-1");
        props.put("mail.aws.host", "localhost");
        props.put("mail.aws.user", getCredentials(Credentials.ACCESS_KEY));
        props.put("mail.aws.password", getCredentials(Credentials.SECRET_ACCESS_KEY));


        // Create a Session object to represent a mail session with the specified properties. 
        final Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        final MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM,FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setSubject(SUBJECT);
        msg.setContent(BODY,"text/html");

        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set
        msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);

        // Create a transport.
        try (final Transport transport = session.getTransport()) {
            System.out.println("Sending...");

            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

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
