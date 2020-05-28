package com.example.auth.shared;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.example.auth.shared.dto.UserDto;

public class AmazonSES {
    private static final String FROM = "raghava.javvaji@gmail.com";
    private static final String TO = "javvaji.raghava@gmail.com";
    private static final String SUBJECT = "One last step to complete your registration with App";
    private static final String PASSWORD_RESET_SUBJECT = "Password reset request";
    private static final String BODY = "Thank you for registering with our app. "
            + " To complete registration process and be able to log in, click on the following link: "
            + " http://localhost:8084/api/users/email-verification?token=$tokenValue"
            + " Final step to complete your registration"
            + "Thank you! And we are waiting for you inside!";
    final String PASSWORD_RESET_TEXTBODY = "A request to reset your password "
            + "Hi, $firstName! "
            + "Someone has requested to reset your password with our project. If it were not you, please ignore it."
            + " otherwise please open the link below in your browser window to set a new password:"
            + " http://localhost:8084/api/password-reset.html?token=$tokenValue"
            + " Thank you!";

    public boolean sendVerificationEmail(UserDto userDto) {
        String bodyWithToken = BODY.replace("$tokenValue", userDto.getEmailVerificationToken());
        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1)
                .build();
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(userDto.getEmail()))
                .withMessage(new Message()
                        .withBody(new Body().withText(new Content().withCharset("UTF-8").withData(bodyWithToken)))
                        .withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
                .withSource(FROM);
        SendEmailResult response = client.sendEmail(request);
        return response != null && (response.getMessageId() != null && !response.getMessageId().isEmpty());
    }

    public boolean sendPasswordResetEmail(String toEmail, String token) {
        String bodyWithToken = PASSWORD_RESET_TEXTBODY.replace("$tokenValue", token);
        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1)
                .build();
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(toEmail))
                .withMessage(new Message()
                        .withBody(new Body().withText(new Content().withCharset("UTF-8").withData(bodyWithToken)))
                        .withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
                .withSource(FROM);
        SendEmailResult response = client.sendEmail(request);
        return response != null && (response.getMessageId() != null && !response.getMessageId().isEmpty());
    }
}
