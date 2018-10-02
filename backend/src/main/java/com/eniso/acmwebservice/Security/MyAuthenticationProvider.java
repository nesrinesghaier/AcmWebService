package com.eniso.acmwebservice.Security;

import com.eniso.acmwebservice.Dao.AcmerRepository;
import com.eniso.acmwebservice.Entity.Acmer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(MyAuthenticationProvider.class);

    @Autowired
    private PasswordEncryption passwordEncryption;
    @Autowired
    private AcmerRepository acmerRepository;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        Acmer user = acmerRepository.findByHandle(name);
        String encryptedPw = null;
        if(user == null) {
            throw new BadCredentialsException("User not found");
        }
        try {
            String salt = passwordEncryption.generateSalt();
            encryptedPw = this.passwordEncryption.getEncryptedPassword(password, salt);//
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Pw decrytion error: ",e);
        }
        if(encryptedPw == null || !encryptedPw.equals(user.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException("User: "+name+" not found.");
        }
        log.info("User: "+name+" logged in.");
        return new UsernamePasswordAuthenticationToken(
                name, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }

}