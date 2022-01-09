package ro.acs.ssa.cc.mailing.mailcreatorservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ro.acs.ssa.cc.mailing.mailcreatorservice.dto.EmailProperties;
import ro.acs.ssa.cc.mailing.mailcreatorservice.dto.UserResponseBody;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@Slf4j
public class MailCreatorApi {
    private final RestTemplate restTemplate;
    private final RabbitTemplate template;
    private final EmailProperties emailProperties;
    private final ObjectMapper objectMapper;
    @Value("${ro.acs.ssa.cc.mailing.send-messages-queue.exchange}")
    private String sendMessagesQueueExchange;
    @Value("${ro.acs.ssa.cc.mailing.send-messages-queue.routingKey}")
    private String sendMessagesQueueRoutingKey;
    @Value("${ro.acs.ssa.cc.mailing.userservice.basePath}")
    private String userServiceBasePath;
    @Value("${ro.acs.ssa.cc.mailing.userservice.paths.user.details.value}")
    private String userServiceUserDetailsPath;

    public MailCreatorApi(RestTemplate restTemplate, RabbitTemplate template, EmailProperties emailProperties, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.template = template;
        this.emailProperties = emailProperties;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "build-messages-queue")
    public void initiateMailout(UserResponseBody user) {
        UserResponseBody userWithDetails = fetchUserDetails(user.getId());

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(emailProperties.getFrom());
        msg.setTo(userWithDetails.getEmail());
        msg.setSubject(emailProperties.getMailTitle());
        msg.setText(String.format("Buna, %s. Aveti '%s' RON in contul dvs. cu IBAN '%s'", userWithDetails.getEmail(), userWithDetails.getAvailableAmount(), userWithDetails.getIban()));

        template.convertAndSend(sendMessagesQueueExchange, sendMessagesQueueRoutingKey, getPayloadAsBase64(msg));
    }

    private Object getPayloadAsBase64(SimpleMailMessage msg) {
        try {
            return Base64.getEncoder().encode(objectMapper.writeValueAsString(msg).getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to base64Encode simpleMailMessage!");
        }
    }

    private UserResponseBody fetchUserDetails(String userId) {
        ResponseEntity<UserResponseBody> usersResponseBody = restTemplate.getForEntity((userServiceBasePath + userServiceUserDetailsPath).replace("{userId}", userId), UserResponseBody.class);
        if (usersResponseBody.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Fetch user details failed!");
        }
        return usersResponseBody.getBody();
    }

}
