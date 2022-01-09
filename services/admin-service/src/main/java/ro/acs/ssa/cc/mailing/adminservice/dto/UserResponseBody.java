package ro.acs.ssa.cc.mailing.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponseBody {
    private String id;
    private String email;
    private String iban;
    private String availableAmount;
}
