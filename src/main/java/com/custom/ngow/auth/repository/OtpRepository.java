package com.custom.ngow.auth.repository;

import com.custom.ngow.auth.enity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {

}
