package ro.acs.ssa.cc.mailing.mailsenderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class SimpleMailObject {
    private String from;
    private String replyTo;
    private List<String> to;
    private String subject;
    private String text;
}
