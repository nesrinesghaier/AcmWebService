package com.eniso.acmwebservice.Service;

import com.eniso.acmwebservice.Dao.AcmerDao;
import com.eniso.acmwebservice.Entity.Acmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AcmerService    {
    @Autowired
    private AcmerDao acmerDao;

    public Collection<Acmer> getAllAcmers(){
        return  acmerDao.getAllAcmers();
    }
    public Acmer getAcmerByHandle(String handle){
        return acmerDao.getAcmerByHandle(handle);
    }


    public void deleteAcmerByHandle(String handle){
        this.acmerDao.deleteAcmerByHandle(handle);
    }

    public void addAcmerByHandle(String handle){
        this.acmerDao.addAcmerByHandle(handle);
    }

   /* public String getAcmerSolvedProblems(String handle) {
        return acmerDao.getAcmerSolvedProblems(handle);
    }*/

}
