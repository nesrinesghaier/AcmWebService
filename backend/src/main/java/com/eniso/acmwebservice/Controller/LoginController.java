package com.eniso.acmwebservice.Controller;

import com.eniso.acmwebservice.Entity.Acmer;
import com.eniso.acmwebservice.Security.JwtTokenUtil;
import com.eniso.acmwebservice.Service.AcmerService;
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

    private final AcmerService acmerService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public LoginController(AcmerService acmerService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.acmerService = acmerService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

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

}
