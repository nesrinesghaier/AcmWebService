package com.eniso.acmwebservice.Service;

import com.eniso.acmwebservice.Dao.AcmerDao;
import com.eniso.acmwebservice.Entity.Acmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface AcmerRepository extends CrudRepository<Acmer, String> {
    Acmer findByHandle(String handle);
    void deleteByHandle(String handle);
}
