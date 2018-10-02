package com.eniso.acmwebservice.Dao;

import com.eniso.acmwebservice.Entity.Acmer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AcmerRepository extends CrudRepository<Acmer, String> {
    @Query("SELECT a from Acmer a where a.handle= :handle")
    Acmer findByHandle(@Param("handle") String handle);

    void deleteByHandle(String handle);

    @Query("SELECT a from Acmer a")
    List<Acmer> findAllAcmers();


}
