package com.eniso.acmwebservice.Controller;

import com.eniso.acmwebservice.Dao.AcmerDao;
import com.eniso.acmwebservice.Entity.Acmer;
import com.eniso.acmwebservice.Service.AcmerRepository;
import com.eniso.acmwebservice.Service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.persistence.EntityManager;
import java.io.*;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/acmers")
@Controller
public class AcmerController {
    @Autowired
    AcmerDao acmerDao;

    @Autowired
    StorageService storageService;
    EntityManager entityManager;
    List<String> files = new ArrayList<String>();
    @Autowired
    private AcmerRepository acmerRepository;

    @GetMapping(value = "")
    public Collection<Acmer> getAllAcmer() {
        List<Acmer> acmerList = new ArrayList<>();
        acmerRepository.findAll().forEach(acmerList::add);
        return acmerList;
    }

    @PostMapping("/createAll")
    public Collection<Acmer> UploadFile(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> itr = request.getFileNames();
        MultipartFile file = request.getFile(itr.next());
        String fileName = file.getOriginalFilename();
        ObjectMapper mapper = new ObjectMapper();
        com.fasterxml.jackson.databind.type.TypeFactory factory = mapper.getTypeFactory();
        CollectionType listType = factory.constructCollectionType(List.class, Acmer.class);
        StringWriter writer = new StringWriter();
        InputStream inputStream = file.getInputStream();
        List<Acmer> list = new ArrayList<>();
        try {
            Scanner sc = new Scanner(inputStream);
            StringBuilder sb = new StringBuilder("");
            while (sc.hasNext()) {
                sb.append(sc.next());
            }
            list = mapper.readValue(sb.toString(), listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            Acmer acmer = list.get(i);
            if(acmer!=null){
                int solved =acmer.getAcmerSolvedProblems(acmer.getHandle());
                acmer.setSolvedProblems(solved);
            }
        }
        acmerRepository.saveAll(list);
        return list;
    }

    @PostMapping(value = "/create")
    public Acmer createAcmer(@RequestBody String handle) throws IOException {
        Acmer _acmer = new Acmer(handle);
        acmerRepository.save(_acmer);
        return _acmer;
    }


    @DeleteMapping("/delete/{handle}")
    public Collection<Acmer> deleteAcmer(@PathVariable("handle") String handle) {
        Acmer acmer = acmerRepository.findByHandle(handle);
        acmerRepository.delete(acmer);
        List<Acmer> acmerList = new ArrayList<>();
        acmerRepository.findAll().forEach(acmerList::add);
        return acmerList;
    }

    @DeleteMapping("/deleteAll")
    public Collection<Acmer> deleteAllAcmers() {
        acmerRepository.deleteAll();
        List<Acmer> acmerList = new ArrayList<>();
        acmerRepository.findAll().forEach(acmerList::add);
        return acmerList;
    }

    @GetMapping(value = "/{handle}")
    public Acmer findByHandle(@PathVariable String handle) {
        Acmer acmer = acmerRepository.findByHandle(handle);
        return acmer;
    }

    @PutMapping("/edit/{handle}")
    public ResponseEntity<Acmer> updateAcmer(@PathVariable("handle") String handle, @RequestBody Acmer acmer) {
        System.out.println("Update Acmer with handle = " + handle + "...");

        Optional<Acmer> customerData = acmerRepository.findById(handle);

        if (customerData.isPresent()) {
            Acmer _acmer = customerData.get();
            _acmer.setEmail(acmer.getEmail());
            _acmer.setCountry(acmer.getCountry());
            _acmer.setFirstName(acmer.getFirstName());
            _acmer.setLastName(acmer.getLastName());
            _acmer.setRole(acmer.getRole());
            return new ResponseEntity<>(acmerRepository.save(_acmer), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
