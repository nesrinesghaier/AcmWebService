package com.eniso.acmwebservice.Service;

import com.eniso.acmwebservice.Dao.AcmerDAO;
import com.eniso.acmwebservice.Dao.AcmerRepository;
import com.eniso.acmwebservice.Dao.ProblemsDetailsRepository;
import com.eniso.acmwebservice.Entity.*;
import com.eniso.acmwebservice.Security.JwtTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.*;

import static com.eniso.acmwebservice.Entity.Constants.TOKEN_PREFIX;

@Transactional
@Service(value = "acmerService")
public class AcmerService implements UserDetailsService {

    @Autowired
    AcmerRepository acmerRepository;

    @Autowired
    ProblemsDetailsRepository problemsDetailsRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    private TaskExecutor taskExecutor;

    private static final Logger logger = LoggerFactory.getLogger(AcmerService.class);

    private AcmerDAO acmerDAO = new AcmerDAO();

    public Acmer findByHandle(String handle) {
        Acmer acmer = acmerRepository.findByHandle(handle);
        if (acmer == null) {
            logger.error("Cannot found Acmer with handle %s!", handle);
            return null;
        }
        logger.info("Acmer found by handle %s successfully.", handle);
        return acmer;
    }

    public List<Acmer> findAllAcmers() {
        logger.info("Get all acmers from DB.");
        return new ArrayList<>(acmerRepository.findAllAcmers());
    }

    public void updateAcmer(Acmer acmer) {
        if (acmer.getPassword().isEmpty()) {
            Acmer dbAcmer = findByHandle(acmer.getHandle());
            if (dbAcmer != null) {
                acmer.setPassword(dbAcmer.getPassword());
            } else {
                logger.error("Echec updating Acmer.");
                return;
            }
        } else {
            String rawPassword = acmer.getPassword();
            String cryptedPassword = bCryptPasswordEncoder.encode(rawPassword);
            acmer.setPassword(cryptedPassword);
        }
        logger.info("Updating Acmer successfully.");
        acmerRepository.save(acmer);
    }

    public boolean deleteAcmer(String handle) {
        Acmer acmer = findByHandle(handle);
        if (acmer != null && !acmer.getRole().equals(Role.ADMIN)) {
            acmerRepository.delete(acmer);
        } else {
            logger.warn("Cannot delete an ADMIN");
            return false;
        }
        logger.info("Acmer deleted successfully.");
        return true;
    }

    public void deleteAllAcmers() {
        List<Acmer> acmerList = findAllAcmers();
        for (Acmer acmer : acmerList) {
            if (!acmer.getRole().equals(Role.ADMIN)) {
                acmerRepository.delete(acmer);
            }
        }
        logger.info("All Acmers are Deleted successfully.");
    }

    public Acmer getAcmerInfosByHandle(String handle) {
        Acmer acmer;
        try {
            if (handle.isEmpty()) {
                return null;
            }
            acmer = acmerDAO.getJsonResult(handle);
            if (acmer != null) {
                if (acmer.getHandle().equals("bacali") || acmer.getHandle().equals("myob-_-")) {
                    acmer.setRole(Role.ADMIN);
                } else {
                    acmer.setRole(Role.USER);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return acmer;
    }

    public boolean createAcmer(Acmer acmerData) {
        Optional<Acmer> acmerObj = acmerRepository.findById(acmerData.getHandle());
        if (acmerObj.isPresent()) {
            logger.error("Error in registering Acmer!");
            return false;
        }
        Acmer acmer = getAcmerInfosByHandle(acmerData.getHandle());
        acmer.setFirstName(acmerData.getFirstName());
        acmer.setLastName(acmerData.getLastName());
        acmer.setEmail(acmerData.getEmail());
        String encryptedPassword = this.bCryptPasswordEncoder.encode(acmerData.getPassword());
        acmer.setPassword(encryptedPassword);
        acmer.setCountry("Tunisia");
        acmerRepository.save(acmer);
        refreshAcmerData(acmer);
        logger.info("Acmer registered successfully!");
        return true;
    }

    public boolean createAll(MultipartFile file, String token) {
        try {
            InputStreamReader reader = new InputStreamReader(file.getInputStream());
            Scanner in = new Scanner(reader);
            StringBuilder sb = new StringBuilder();
            while (in.hasNext()) {
                sb.append(in.nextLine());
            }
            ObjectMapper mapper = new ObjectMapper();
            TypeFactory factory = mapper.getTypeFactory();
            CollectionType listType = factory.constructCollectionType(List.class, Acmer.class);
            List<Acmer> acmerList = mapper.readValue(sb.toString(), listType);
            String loggedInUserHandle = jwtTokenUtil.getUsernameFromToken(token.replace(TOKEN_PREFIX, ""));
            for (Acmer acmer : acmerList) {
                if (acmer.getHandle().equals(loggedInUserHandle)
                        || acmerRepository.findByHandle(acmer.getHandle()) != null) {
                    continue;
                }
                String handle = acmer.getHandle();
                String password = acmer.getPassword();
                acmer = getAcmerInfosByHandle(handle);
                if (acmer != null) {
                    acmer.setPassword(password);
                    acmerRepository.save(acmer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in adding Acmers!");
            return false;
        }
        logger.info("Adding Acmers successfully.");
        return true;
    }

    public void populateAdmins() {
        if (acmerRepository.findByHandle("bacali") == null) {
            Acmer acmer = getAcmerInfosByHandle("bacali");
            acmer.setPassword(this.bCryptPasswordEncoder.encode("95253834"));
            acmerRepository.save(acmer);
            refreshAcmerData(acmer);
        }
        if (acmerRepository.findByHandle("myob-_-") == null) {
            Acmer acmer = getAcmerInfosByHandle("myob-_-");
            acmer.setFirstName("Nesrine");
            acmer.setLastName("Sghaier");
            acmer.setEmail("nesrinesghaier10@gmail.com");
            acmer.setPassword(this.bCryptPasswordEncoder.encode("50609713"));
            refreshAcmerData(acmer);
            acmerRepository.save(acmer);
        }
        logger.info("All Admins added successfully");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Acmer acmer = acmerRepository.findByHandle(username);
        if (acmer == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(acmer.getUsername(), acmer.getPassword(), acmer.getAuthorities());
    }

    void updateAcmersScore() {
        List<Acmer> acmersList = acmerRepository.findAllAcmers();
        Map<Integer, Contest> contests = acmerDAO.getAllAvailableContests();
        for (Acmer acmer : acmersList) {
            updateAcmerScore(acmer, contests);
        }
    }

    @Transactional
    public void updateAcmerScore(Acmer acmer, Map<Integer, Contest> contests) {
        SubmissionWrapper result = acmerDAO.getSubmissionResult(acmer);
        Map<String, Integer> problemsCount = new HashMap<>();
        problemsCount.put("A", 0);
        problemsCount.put("B", 0);
        problemsCount.put("C", 0);
        problemsCount.put("D", 0);
        problemsCount.put("E", 0);
        problemsCount.put("F", 0);
        problemsCount.put("G", 0);
        problemsCount.put("H", 0);
        if (result.getResult() != null) {
            Set<String> set = new HashSet();
            for (int i = 0; i < result.getResult().size(); i++) {
                Submission submission = result.getResult().get(i);
                int contestId = submission.getContestId();
                if (contests.containsKey(contestId) && submission.getVerdict().equals("OK")) {
                    Problem problem = submission.getProblem();
                    char pbIndex = problem.getIndex().charAt(0);
                    Contest contest = contests.get(contestId);
                    if (contest.getName().contains("div. 3")) {
                        pbIndex = (char) Math.max('A', pbIndex - 2);
                    } else if (contest.getName().contains("div. 1")) {
                        pbIndex = (char) Math.min('H', pbIndex + 2);
                    }
                    if (!set.contains(problem.getName())) {
                        set.add(problem.getName());
                        String key = String.valueOf(pbIndex);
                        if (problemsCount.containsKey(key)) {
                            problemsCount.put(key, problemsCount.get(key) + 1);
                        }
                    }
                }
            }
            int size = set.size();
            if (size > acmer.getSolvedProblems()) {
                int score = calculScore(problemsCount);
                acmer.setScore(score);
                acmer.setSolvedProblems(size);
                Collection<ProblemsDetails> problemsDetails = new ArrayList<>();
                for (String problemIndex : problemsCount.keySet()) {
                    problemsDetails.add(new ProblemsDetails(problemIndex, problemsCount.get(problemIndex)));
                }
                acmer.setSolvedProblemsDetails(problemsDetails);
                acmerRepository.save(acmer);
            }
        }
    }

    private int calculScore(Map<String, Integer> problemsCount) {
        Map<String, Integer> echelle = new HashMap<>();
        echelle.put("A", 1);
        echelle.put("B", 2);
        echelle.put("C", 3);
        echelle.put("D", 5);
        echelle.put("E", 8);
        echelle.put("F", 13);
        echelle.put("G", 21);
        echelle.put("H", 34);
        int score = 0;
        for (String pbIndex : problemsCount.keySet()) {
            if (echelle.containsKey(pbIndex)) {
                score += echelle.get(pbIndex) * problemsCount.get(pbIndex);
            }
        }
        return score;
    }


    public void refreshAcmerData(Acmer acmer) {
        taskExecutor.execute(() -> {
            String handle = acmer.getHandle();
            logger.info("Refreshing data for %s begin now!".replace("%s", handle));
            Map<Integer, Contest> contests = acmerDAO.getAllAvailableContests();
            updateAcmerScore(acmer, contests);
            logger.info("Data of %s was successfully refreshed!".replace("%s", handle));
        });
    }
}

