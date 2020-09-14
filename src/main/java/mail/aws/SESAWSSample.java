package mail.aws;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import mail.aws.credentials.DummyData;

/**
 * test.
 */
public class SESAWSSample {

    // The HTML body for the email.
    private static final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
            + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
            + "AWS SDK for Java</a>";

    // The email body for recipients with non-HTML email clients.
    private static final String TEXTBODY = "This email was sent through Amazon SES "
            + "using the AWS SDK for Java.";

    public static void main(String[] args) {

        final AmazonSimpleEmailService client =
                AmazonSimpleEmailServiceClientBuilder.standard()
                        // Replace US_WEST_2 with the AWS Region you're using for
                        // Amazon SES.
                        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                                "http://localhost:9001", "us-east-1"))
                        .build();
        final SendEmailRequest request = new SendEmailRequest()
                .withSource(DummyData.FROM)
                .withDestination(
                        new Destination().withToAddresses(DummyData.TO))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8").withData(SESAWSSample.HTMLBODY))
                                .withText(new Content()
                                        .withCharset("UTF-8").withData(SESAWSSample.TEXTBODY)))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(DummyData.SUBJECT)))
                // Comment or remove the next line if you are not using a
                // configuration set
                .withConfigurationSetName(DummyData.CONFIGSET);

        try {
            final SendEmailResult sendEmailResult = client.sendEmail(request);
            System.out.println("Email sent!");
            System.out.println("The result is:");
            System.out.println(sendEmailResult.toString());
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
        }
        System.exit(0);
    }
}
