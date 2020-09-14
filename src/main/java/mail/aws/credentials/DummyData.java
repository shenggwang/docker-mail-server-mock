package mail.aws.credentials;

public class DummyData {

    // Replace sender@example.com with your "From" address.
    // This address must be verified.
    public static final String FROM = "sender@example.com";
    public static final String FROM_NAME = "Sender Name";

    // Replace recipient@example.com with a "To" address. If your account
    // is still in the sandbox, this address must be verified.
    public static final String TO = "recipient@example.com";


    // Replace smtp_username with your Amazon SES SMTP user name.
    public static final String SMTP_USERNAME = "smtp_user";

    // Replace smtp_password with your Amazon SES SMTP password.
    public static final String SMTP_PASSWORD = "smtp_pass";

    // The name of the Configuration Set to use for this message.
    // If you comment out or remove this variable, you will also need to
    // comment out or remove the header below.
    public static final String CONFIGSET = "ConfigSet";

    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    // See https://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
    // for more information.
    public static final String HOST = "http://localhost:9001";

    // The port you will connect to on the Amazon SES SMTP endpoint.
    public static final int PORT = 9001;

    public static final String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";
}
