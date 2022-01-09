package ro.acs.ssa.cc.mailing.userservice.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ro.acs.ssa.cc.mailing.userservice.dto.AllUsersResponseBody;
import ro.acs.ssa.cc.mailing.userservice.dto.UserResponseBody;
import ro.acs.ssa.cc.mailing.userservice.entity.BankUserEntity;
import ro.acs.ssa.cc.mailing.userservice.repository.BankUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class UsersController {
    private final BankUserRepository bankUserRepository;

    public UsersController(BankUserRepository bankUserRepository) {
        this.bankUserRepository = bankUserRepository;
    }

    @GetMapping("/users")
    @ResponseStatus(code = HttpStatus.OK)
    public AllUsersResponseBody getExistingUser() {
        List<UserResponseBody> users = new ArrayList<>();
        bankUserRepository.findAll().forEach(user -> users.add(UserResponseBody.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build()));
        return AllUsersResponseBody.builder().users(users).build();
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserResponseBody getExistingUser(@PathVariable String userId) {
        Optional<BankUserEntity> userOptional = bankUserRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User with id was not found!");
        }
        BankUserEntity user = userOptional.get();
        return UserResponseBody.builder()
                .id(user.getId())
                .email(user.getEmail())
                .iban(user.getIban())
                .availableAmount(user.getAvailableAmount())
                .build();
    }
}
