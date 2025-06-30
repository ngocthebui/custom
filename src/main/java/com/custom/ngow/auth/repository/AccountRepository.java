package com.custom.ngow.auth.repository;

import com.custom.ngow.auth.enity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByUsername(String username);

}
