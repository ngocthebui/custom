package com.custom.ngow.auth.service;

import com.custom.ngow.auth.constant.AccountStatus;
import com.custom.ngow.auth.dto.request.AuthenticationRequest;
import com.custom.ngow.auth.dto.response.AuthenticationResponse;
import com.custom.ngow.auth.enity.Account;
import com.custom.ngow.auth.repository.AccountRepository;
import com.custom.ngow.common.constant.ErrorCode;
import com.custom.ngow.common.exception.ForwardException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.signer_key}")
    private String signerKey;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ForwardException(ErrorCode.E404101));

        boolean authenticated = passwordEncoder.matches(request.getPassword(),
                account.getPassword());

        if (!authenticated) {
            throw new ForwardException(ErrorCode.E401100, "Your password is not correct");
        }

        if (!StringUtils.equals(account.getStatus(), AccountStatus.ACTIVE.name())) {
            throw new ForwardException(ErrorCode.E403100, "Account is not active!");
        }

        String token = generateToken(account);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    private String generateToken(Account account) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .issuer("nthe.bui@gmail.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", account.getRole())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new ForwardException(ErrorCode.E500100, "Can not generate Token");
        }
    }
}
