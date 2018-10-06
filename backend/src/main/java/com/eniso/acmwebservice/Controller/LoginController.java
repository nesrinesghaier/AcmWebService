package com.eniso.acmwebservice.Controller;

import com.eniso.acmwebservice.Entity.Acmer;
import com.eniso.acmwebservice.Security.JwtTokenUtil;
import com.eniso.acmwebservice.Service.AcmerService;
import com.eniso.acmwebservice.Service.AsynchronousService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    AcmerService acmerService;

    @Autowired
    AsynchronousService asynchronousService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(AcmerController.class);

    @PostMapping(value = "/login")
    public ResponseEntity<Acmer> login(@RequestBody Acmer loginUser) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Acmer acmer = acmerService.findByHandle(loginUser.getUsername());
        String token = jwtTokenUtil.generateToken(acmer);
        acmer.setToken(token);
        acmerService.updateAcmer(acmer);
        acmer.setPassword("****");
        return new ResponseEntity<>(acmer, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Void> register(@RequestBody Acmer registerAcmer) {
        boolean bool = acmerService.createAcmer(registerAcmer);
        if (!bool) {
            logger.error("Error in registering Acmer!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        asynchronousService.refreshAcmerData(acmerService.findByHandle(registerAcmer.getHandle()));
        logger.info("Acmer registered successfully!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
