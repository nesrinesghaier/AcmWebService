package com.eniso.acmwebservice.Service;

import com.eniso.acmwebservice.Dao.AcmerDAO;
import com.eniso.acmwebservice.Dao.AcmerRepository;
import com.eniso.acmwebservice.Entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AsynchronousService {

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    AcmerRepository acmerRepository;

    @Autowired
    AcmerService acmerService;

    private AcmerDAO acmerDAO = new AcmerDAO();

    private static final Logger logger = LoggerFactory.getLogger(AsynchronousService.class);

//    @Scheduled(fixedDelay = Constants.REFRESH_DATA_DELAY_SECONDS * 1000)
//    public void refreshData() {
//        logger.info("Starting updating data...");
//        acmerService.updateAcmersScore();
//        logger.info("Updating data finished successfully");
//    }

    public void refreshAcmerData(Acmer acmer) {
        taskExecutor.execute(() -> {
            String handle = acmer.getHandle();
            logger.info("Refreshing data for %s begin now!".replace("%s", handle));
            Map<Integer, Contest> contests = acmerDAO.getAllAvailableContests();
            acmerService.updateAcmerScore(acmer, contests);
            logger.info("Data of %s was successfully refreshed!".replace("%s", handle));
        });
    }


}
