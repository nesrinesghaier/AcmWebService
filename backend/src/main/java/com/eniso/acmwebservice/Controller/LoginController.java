package com.eniso.acmwebservice.Controller;

import com.eniso.acmwebservice.Entity.Acmer;
import com.eniso.acmwebservice.Service.AcmerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private AcmerService acmerService;

    @PostMapping("/login")
    public ResponseEntity<Acmer> login(@RequestBody String acmerData) {
        Acmer loggedAcmer = acmerService.login(acmerData);
        if (loggedAcmer.getHandle() == null) {
            System.out.println(loggedAcmer.toString());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(loggedAcmer, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "ok";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }
}
