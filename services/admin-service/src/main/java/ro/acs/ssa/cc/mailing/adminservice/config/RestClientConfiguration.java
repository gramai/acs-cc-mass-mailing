package ro.acs.ssa.cc.mailing.adminservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfiguration {
    @Bean(name = "userServiceRestTemplate")
    public RestTemplate userServiceRestTemplate() {
        return new RestTemplate();
    }
}
