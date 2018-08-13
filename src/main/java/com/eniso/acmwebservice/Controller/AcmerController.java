package com.eniso.acmwebservice.Controller;

import com.eniso.acmwebservice.Dao.AcmerDao;
import com.eniso.acmwebservice.Entity.Acmer;
import com.eniso.acmwebservice.Service.AcmerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/acmers")
@Controller
public class AcmerController {
    @Autowired
    AcmerDao acmerDao;

    @Autowired
    private AcmerService acmerService;

    @GetMapping(value="")
        public Collection<Acmer> getAllAcmer(){
        return  acmerService.getAllAcmers();
    }

    @GetMapping(value="/{handle}")
    public Acmer getAcmerByHandle(@PathVariable("handle") String handle){
        return acmerService.getAcmerByHandle(handle);
    }

    @DeleteMapping(value = "/{handle}")
    public void deleteAcmerByHandle(@PathVariable("handle") String handle){
        this.acmerService.deleteAcmerByHandle(handle);
    }

    @GetMapping(value = "/{handle}/add")
    public void addAcmerByHandle(@PathVariable("handle") String handle) throws IOException {
        this.acmerDao.addAcmerByHandle(handle);
    }
/*
    @PostMapping(value="/Acmers/add")
    public  Acmer addAcmer(@RequestBody Acmer acmer){
        Acmer _acmer = acmerDao.save(new Acmer(acmer.getHandle(),acmer.getEmail(),acmer.getFirstName(),acmer.getLastName(),
                acmer.getCountry(),acmer.getRank(),acmer.getMaxRank(),acmer.getRating(),acmer.getMaxRating(),acmer.getProblemSolved()));
        return _acmer;
    }

    @DeleteMapping("/Acmers/{handle}")
    public ResponseEntity<String> deleteAcmer(@PathVariable("handle") String handle) {

        System.out.println("Delete Customer with handle = " + handle + "...");

        acmerDao.deleteById(handle);

        return new ResponseEntity<>("Customer has been deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/Acmers/delete")
    public ResponseEntity<String> deleteAllCustomers() {
        System.out.println("Delete All Acmers...");

        acmerDao.deleteAll();

        return new ResponseEntity<>("All Acmers have been deleted!", HttpStatus.OK);
    }
    @GetMapping(value = "acmers/{handle}")
    public Acmer findByHundle(@PathVariable String handle) {

        Acmer acmer = acmerDao.findAcmerByHandle(handle);
        return acmer ;
    }


    /*@RequestMapping(value = "/{handle}",method = RequestMethod.GET)
    public String getAcmerByHandle(@PathVariable("handle") String handle) {
        return acmerService.getAcmerSolvedProblems(handle);
    }*/
}
