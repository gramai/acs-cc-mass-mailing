package ro.acs.ssa.cc.mailing.mailcreatorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class AllUsersResponseBody {
    List<UserResponseBody> users;
}
