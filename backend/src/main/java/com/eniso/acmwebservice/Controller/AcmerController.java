package com.eniso.acmwebservice.Controller;

import com.eniso.acmwebservice.Entity.Acmer;
import com.eniso.acmwebservice.Entity.Constants;
import com.eniso.acmwebservice.Service.AcmerService;
import com.eniso.acmwebservice.Service.AsynchronousService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.eniso.acmwebservice.Entity.Constants.HEADER_STRING;

@RestController
@RequestMapping("/api/acmers")
@CrossOrigin(origins = "*")
public class AcmerController {

    @Autowired
    AcmerService acmerService;

    @Autowired
    AsynchronousService asynchronousService;

    private static final Logger logger = LoggerFactory.getLogger(AcmerController.class);

    @GetMapping(value = "")
    public ResponseEntity<Collection<Acmer>> getAllAcmer() {
        List<Acmer> acmerList = new ArrayList<>(acmerService.findAllAcmers());
        logger.info("Get all acmers from DB.");
        return new ResponseEntity<>(acmerList, HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Void> createAcmer(@RequestBody Acmer acmerData) {
        boolean bool = acmerService.createAcmer(acmerData);
        if (!bool) {
            logger.error("Error in adding Acmer!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        asynchronousService.refreshAcmerData(acmerService.findByHandle(acmerData.getHandle()));
        logger.info("Adding Acmer successfully.");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/createAll")
    public ResponseEntity<Void> createAll(MultipartHttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        boolean test = acmerService.createAll(request.getFile(request.getFileNames().next()), token);
        if (!test) {
            logger.error("Error in adding Acmers!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Adding Acmers successfully.");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{handle:.+}")
    public ResponseEntity<Collection<Acmer>> deleteAcmer(@PathVariable("handle") String handle) {
        boolean bool = acmerService.deleteAcmer(handle);
        List<Acmer> acmerList = acmerService.findAllAcmers();
        if (!bool) {
            logger.warn("Cannot delete an ADMIN");
            return new ResponseEntity<>(acmerList, HttpStatus.FORBIDDEN);
        }
        logger.info("Acmer deleted successfully.");
        return new ResponseEntity<>(acmerList, HttpStatus.OK);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Collection<Acmer>> deleteAllAcmers() {
        acmerService.deleteAllAcmers();
        List<Acmer> acmerList = acmerService.findAllAcmers();
        logger.info("Deleting all Acmers successfully.");
        return new ResponseEntity<>(acmerList, HttpStatus.OK);
    }

    @GetMapping(value = "/{handle:.+}")
    public ResponseEntity<Acmer> findByHandle(@PathVariable String handle) {
        Acmer acmer = acmerService.findByHandle(handle);
        if (acmer == null) {
            logger.error("Can't find Acmer by handle %s!", handle);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Acmer found by handle %s successfully.", handle);
        return new ResponseEntity<>(acmer, HttpStatus.OK);
    }

    @PutMapping(value = "")
    public ResponseEntity<Void> updateAcmer(@RequestBody Acmer acmer) {
        acmerService.updateAcmer(acmer);
        logger.info("Updating Acmer successfully.");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
