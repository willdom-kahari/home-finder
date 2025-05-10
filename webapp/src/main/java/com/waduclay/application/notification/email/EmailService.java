package com.waduclay.application.notification.email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String emailSender;

    @Async
    public void sendOtp(
            String destinationEmail,
            String customerName,
            String otp
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom(emailSender);
//        final String templateName = EmailTemplates.OTP.getTemplate();

        Map<String, Object> variables = Map.of(
                "customerName", customerName,
                "otp", otp
        );

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject("GREETINGS");

        try {
//            String htmlTemplate = templateEngine.process(templateName, context);
//            messageHelper.setText(htmlTemplate, true);
            messageHelper.setText("Code from your own domain");
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("OTP email sent successfully to: {}", destinationEmail);
        } catch (MessagingException e) {
            log.warn("Cannot send email to: {}", destinationEmail);
        }
    }

}
