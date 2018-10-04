package com.eniso.acmwebservice.Controller;

import com.eniso.acmwebservice.Dao.StorageService;
import com.eniso.acmwebservice.Entity.Acmer;
import com.eniso.acmwebservice.Service.AcmerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/acmers")
@CrossOrigin(origins = "*")
public class AcmerController {

    private final StorageService storageService;

    private final AcmerService acmerService;

    private static final Logger logger = LoggerFactory.getLogger(AcmerController.class);

    @Autowired
    public AcmerController(StorageService storageService, AcmerService acmerService) {
        this.storageService = storageService;
        this.acmerService = acmerService;
    }

    @GetMapping(value = "")
    public ResponseEntity<Collection<Acmer>> getAllAcmer() {
        List<Acmer> acmerList = new ArrayList<>(acmerService.findAllAcmers());
        logger.info("Get all acmers from DB.");
        return new ResponseEntity<>(acmerList, HttpStatus.OK);
    }

    @PostMapping("/createAll")
    public ResponseEntity<Void> createAll(MultipartHttpServletRequest request) {
        boolean test = acmerService.createAll(request.getFile(request.getFileNames().next()));
        if (test) {
            logger.error("Error in adding Acmers!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Adding Acmers successfully.");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Void> createAcmer(@RequestBody String acmerData) {
        boolean bool = acmerService.createAcmer(acmerData);
        if (!bool) {
            logger.error("Error in adding Acmer!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        logger.info("Adding Acmer successfully.");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{handle:.+}")
    public ResponseEntity<Collection<Acmer>> deleteAcmer(@PathVariable("handle") String handle) {
        List<Acmer> acmerList = new ArrayList<>(acmerService.deleteAcmer(handle));
        logger.info("Deleting Acmer successfully.");
        return new ResponseEntity<>(acmerList, HttpStatus.OK);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Collection<Acmer>> deleteAllAcmers() {
        List<Acmer> acmerList = new ArrayList<>(acmerService.deleteAllAcmers());
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
