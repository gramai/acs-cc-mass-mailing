package ro.acs.ssa.cc.mailing.adminservice.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ro.acs.ssa.cc.mailing.adminservice.dto.AllUsersResponseBody;

@RestController
@Slf4j
public class MailController {
    private final RestTemplate restTemplate;
    private final RabbitTemplate template;
    @Value("${ro.acs.ssa.cc.mailing.build-messages-queue.exchange}")
    private String buildMessagesQueueExchange;
    @Value("${ro.acs.ssa.cc.mailing.build-messages-queue.routingKey}")
    private String buildMessagesQueueRoutingKey;
    @Value("${ro.acs.ssa.cc.mailing.userservice.basePath}")
    private String userServiceBasePath;
    @Value("${ro.acs.ssa.cc.mailing.userservice.paths.users.value}")
    private String userServiceUsersPath;

    public MailController(RestTemplate restTemplate, RabbitTemplate template) {
        this.restTemplate = restTemplate;
        this.template = template;
    }

    @PostMapping("/mailout")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void initiateMailout() {
        AllUsersResponseBody users = fetchAllUsers();
        users.getUsers().forEach(user -> template.convertAndSend(buildMessagesQueueExchange, buildMessagesQueueRoutingKey, user));
    }

    private AllUsersResponseBody fetchAllUsers() {
        ResponseEntity<AllUsersResponseBody> usersResponseBody = restTemplate.getForEntity(userServiceBasePath + userServiceUsersPath, AllUsersResponseBody.class);
        if (usersResponseBody.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Fetch users unsuccessful!");
        }
        return usersResponseBody.getBody();
    }

}
