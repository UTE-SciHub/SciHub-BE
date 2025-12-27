package vn.thanhtuanle.common.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.model.response.EmailRecipient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MailService {

    JavaMailSender javaMailSender;

    @Async("emailExecutor")
    public void sendFeedbackNotificationEmail(
            List<EmailRecipient> recipients,
            String topicName,
            String councilName,
            String reviewComments,
            String milestoneDescription,
            String reviewDate,
            String templatePath,
            String viewDetailsUrl
    ) throws MessagingException {
        log.info("Sending feedback notification email to members: {}", recipients);

        String baseHtmlContent = loadHtmlTemplate(templatePath);
        String subject = "Thông báo đánh giá mới - " + councilName;

        for (EmailRecipient recipient : recipients) {
            String escapedHtmlContent = baseHtmlContent.replaceAll("%(?!s)", "%%");

            String htmlContent = String.format(escapedHtmlContent,
                    recipient.getName(),
                    councilName,
                    topicName,
                    reviewComments,
                    milestoneDescription,
                    reviewDate,
                    viewDetailsUrl
            );

            sendEmail(recipient.getEmail(), subject, htmlContent);
        }
    }

    @Async("emailExecutor")
    public void sendCouncilMemberNotificationEmail(
            List<EmailRecipient> recipients,
            String councilName,
            String decisionNumber,
            String establishmentDate,
            String startDate,
            String endDate,
            String notes,
            String templatePath,
            String viewDetailsUrl,
            Map<String, String> memberRoles
    ) throws MessagingException {
        log.info("Sending council member notification email to members: {}", recipients);

        String baseHtmlContent = loadHtmlTemplate(templatePath);
        String subject = "Thông báo tham gia hội đồng - " + councilName;
        String escapedHtmlContent = baseHtmlContent.replaceAll("%(?!s)", "%%");

        for (EmailRecipient recipient : recipients) {
            String role = memberRoles.getOrDefault(recipient.getEmail(), "Thành viên");
            String htmlContent = String.format(escapedHtmlContent,
                    recipient.getName(),
                    councilName,
                    role,
                    councilName,
                    decisionNumber,
                    establishmentDate,
                    startDate,
                    endDate,
                    notes != null ? notes : "Không có ghi chú",
                    viewDetailsUrl
            );

            sendEmail(recipient.getEmail(), subject, htmlContent);
        }
    }

    @Async("emailExecutor")
    public void sendTopicAcceptedNotificationEmail(String recipientEmail, String recipientName, String topicName, String topicCode, String councilName, String evaluationDate, String notes, String viewDetailsUrl) throws MessagingException {
        log.info("Sending topic accepted notification email to: {}", recipientEmail);

        String baseHtmlContent = loadHtmlTemplate("/template/topic-accepted-notification.html");
        String subject = "Thông báo chấp thuận đề tài - " + topicName;

        String escapedHtmlContent = baseHtmlContent.replaceAll("%(?!s)", "%%");
        String formattedDate = formatDateTime(LocalDateTime.parse(evaluationDate));

        String htmlContent = String.format(escapedHtmlContent,
                recipientName,
                topicName,
                topicCode,
                councilName,
                formattedDate,
                notes != null ? notes : "Không có ghi chú",
                viewDetailsUrl
        );

        sendEmail(recipientEmail, subject, htmlContent);
    }

    @Async("emailExecutor")
    public void sendTopicRejectedNotificationEmail(String recipientEmail, String recipientName, String topicName, String topicCode, String councilName, String evaluationDate, String notes, String viewDetailsUrl) throws MessagingException {
        log.info("Sending topic rejected notification email to: {}", recipientEmail);

        String baseHtmlContent = loadHtmlTemplate("/template/topic-rejected-notification.html");
        String subject = "Thông báo từ chối đề tài - " + topicName;

        String escapedHtmlContent = baseHtmlContent.replaceAll("%(?!s)", "%%");
        String formattedDate = formatDateTime(LocalDateTime.parse(evaluationDate));

        String htmlContent = String.format(escapedHtmlContent,
                recipientName,
                topicName,
                topicCode,
                councilName,
                formattedDate,
                notes != null ? notes : "Không có ghi chú",
                viewDetailsUrl
        );

        sendEmail(recipientEmail, subject, htmlContent);
    }

    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        javaMailSender.send(message);
        log.info("Email sent successfully to: {}", to);
    }

    public String loadHtmlTemplate(String path) {
        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("Cannot find template file: " + path);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a"));
    }
}
