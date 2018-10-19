package com.eniso.acmwebservice.Dao;

import com.eniso.acmwebservice.Entity.ProblemsDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemsDetailsRepository extends CrudRepository<ProblemsDetails, Long> {
}
