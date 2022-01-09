package ro.acs.ssa.cc.mailing.mailsenderservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RestController;
import ro.acs.ssa.cc.mailing.mailsenderservice.dto.SimpleMailObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@RestController
@Slf4j
public class MailSenderApi {
    private final JavaMailSender javaMailSender;
    private final ObjectMapper objectMapper;

    public MailSenderApi(JavaMailSender javaMailSender, ObjectMapper objectMapper) {
        this.javaMailSender = javaMailSender;
        this.objectMapper = objectMapper;
    }


    @RabbitListener(queues = "send-messages-queue")
    public void sendEmail(String base64EncodedSimpleMailMessage) {
        SimpleMailObject extractedMessage = extractMessageFromEncodedString(base64EncodedSimpleMailMessage);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(extractedMessage.getFrom());
        msg.setTo(extractedMessage.getTo().get(0));
        msg.setSubject(extractedMessage.getSubject());
        msg.setText(extractedMessage.getText());
        this.sendMessage(msg);
    }

    private SimpleMailObject extractMessageFromEncodedString(String base64EncodedSimpleMailMessage) {
        String decodedMailMessage = decodeMessage(decodeMessage(base64EncodedSimpleMailMessage));
        try {
            return objectMapper.readValue(decodedMailMessage, SimpleMailObject.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Object parsing failed!");
        }
    }

    private String decodeMessage(String base64EncodedSimpleMailMessage) {
        return new String(Base64.getDecoder().decode(base64EncodedSimpleMailMessage.replace("\"", "")), StandardCharsets.UTF_8);
    }

    public void sendMessage(SimpleMailMessage msg) {
        try {
            javaMailSender.send(msg);
            log.debug("The email has been sent");
        } catch (Exception exception) {
            log.debug("The email was not sent, reason:" + exception.getMessage());
            throw new RuntimeException("Unable to send email!");
        }
    }

}
