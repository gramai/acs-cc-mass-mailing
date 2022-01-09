package ro.acs.ssa.cc.mailing.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ro.acs.ssa.cc.mailing.userservice.entity.BankUserEntity;

@Transactional
public interface BankUserRepository extends JpaRepository<BankUserEntity, String> {
}
