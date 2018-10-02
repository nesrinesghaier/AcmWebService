package com.eniso.acmwebservice.Controller;

import com.eniso.acmwebservice.Dao.StorageService;
import com.eniso.acmwebservice.Entity.Acmer;
import com.eniso.acmwebservice.Service.AcmerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/api/acmers")
@CrossOrigin(origins = "*",allowedHeaders={"x-auth-token", "x-requested-with", "x-xsrf-token"})
public class AcmerController {

    public static final Logger logger = LoggerFactory.getLogger(AcmerController.class);
    @Autowired
    StorageService storageService;
    @Autowired
    private AcmerService acmerService;

    @GetMapping(value = "")
    public ResponseEntity<Collection<Acmer>> getAllAcmer() {
        List<Acmer> acmerList = new ArrayList<>(acmerService.findAllAcmers());
        System.out.println(acmerList);
        return new ResponseEntity<>(acmerList, HttpStatus.OK);
    }

    @PostMapping("/createAll")
    public ResponseEntity<Void> createAll(MultipartHttpServletRequest request) {
        try {
            // v2c
            Iterator<String> itr = request.getFileNames();
            MultipartFile file = request.getFile(itr.next());
            InputStream inputStream = file.getInputStream();
            Scanner sc = new Scanner(inputStream);
            StringBuilder sb = new StringBuilder("");
            while (sc.hasNext()) {
                sb.append(sc.next());
            }
            ObjectMapper mapper = new ObjectMapper();
            TypeFactory factory = mapper.getTypeFactory();
            CollectionType listType = factory.constructCollectionType(List.class, Acmer.class);
            List<Acmer> list = mapper.readValue(sb.toString(), listType);
            // service
            acmerService.createAll(list);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Void> createAcmer(@RequestBody String acmerData) {
        boolean bool = acmerService.createAcmer(acmerData);
        if (!bool) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{handle:.+}")
    public ResponseEntity<Collection<Acmer>> deleteAcmer(@PathVariable("handle") String handle) {
        System.out.println(handle);
        List<Acmer> acmerList = new ArrayList<>(acmerService.deleteAcmer(handle));
        return new ResponseEntity<>(acmerList, HttpStatus.OK);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Collection<Acmer>> deleteAllAcmers() {
        List<Acmer> acmerList = new ArrayList<>(acmerService.deleteAllAcmers());
        return new ResponseEntity<>(acmerList, HttpStatus.OK);
    }

    @GetMapping(value = "/{handle:.+}")
    public ResponseEntity<Acmer> findByHandle(@PathVariable String handle) {
        Acmer acmer = null;
        try {
            acmer = acmerService.findByHandle(handle);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(acmer, HttpStatus.OK);
    }

    @PutMapping(value = "")
    public ResponseEntity<Void> updateAcmer(@RequestBody Acmer acmer) {
        acmerService.updateAcmer(acmer);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
