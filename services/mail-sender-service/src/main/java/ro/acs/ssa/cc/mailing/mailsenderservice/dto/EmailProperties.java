package ro.acs.ssa.cc.mailing.mailsenderservice.dto;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class EmailProperties{
    @Value("${ro.acs.ssa.cc.mailing.password.reset.from}")
    private String from;
    @Value("${ro.acs.ssa.cc.mailing.password.reset.title}")
    private String mailTitle;
}
