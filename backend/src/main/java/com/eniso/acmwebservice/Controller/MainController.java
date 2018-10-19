package com.eniso.acmwebservice.Controller;

import com.eniso.acmwebservice.Service.AcmerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class MainController {

    private final AcmerService acmerService;

    @Autowired
    public MainController(AcmerService acmerService) {
        this.acmerService = acmerService;
    }

    @GetMapping(value = {"/", "/login","/register", "/acmers"})
    public String index() {
        return "forward:/index.html";
    }


    @GetMapping("/populate-admins")
    public ResponseEntity<String> populateAdmins() {
        acmerService.populateAdmins();
        return new ResponseEntity<>("DONE", HttpStatus.OK);
    }

}
