package com.eniso.acmwebservice.Service;

import com.eniso.acmwebservice.Dao.AcmerDAO;
import com.eniso.acmwebservice.Dao.AcmerRepository;
import com.eniso.acmwebservice.Entity.*;
import com.eniso.acmwebservice.Security.JwtTokenProvider;
import com.eniso.acmwebservice.Security.PasswordEncryption;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class AcmerService {


    @Autowired
    private AcmerRepository acmerRepository;
    @Autowired
    private PasswordEncryption passwordEncryption;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private AcmerDAO acmerDAO = new AcmerDAO();

    public Acmer findByHandle(String handle) throws IOException {
        Acmer acmer = acmerRepository.findByHandle(handle);
        if (acmer == null) {
            return getAcmerInfosByHandle(handle);
        }
        return acmer;
    }

    public List<Acmer> findAllAcmers() {
        List<Acmer> acmerList = new ArrayList<>();
        acmerRepository.findAll().forEach(acmerList::add);
        return acmerList;
    }

    public void updateAcmer(Acmer acmer) {
        acmerRepository.save(acmer);
    }

    public Collection<Acmer> deleteAcmer(String handle) {
        Acmer acmer = acmerRepository.findByHandle(handle);
        if (acmer != null) {
            acmerRepository.delete(acmer);
        }
        return findAllAcmers();
    }

    public Collection<Acmer> deleteAllAcmers() {
        acmerRepository.deleteAll();
        return findAllAcmers();
    }

    public Acmer getAcmerInfosByHandle(String handle) {
        Acmer acmer = null;
        try {
            if (handle.isEmpty()) {
                return null;
            }
            acmer = acmerDAO.getJsonResult(handle);
            if (acmer.getHandle().equals("bacali") || acmer.getHandle().equals("myob-_-")) {
                acmer.setRole(Role.ADMIN);
            } else {
                acmer.setRole(Role.USER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acmer;
    }

    public void updateAcmersScore() {
        List<Acmer> acmersList = findAllAcmers();
        Map<Integer, Contest> contests = acmerDAO.getAllAvailableContests();
        for (Acmer acmer : acmersList) {
            updateAcmerScore(acmer, contests);
        }
    }

    private void updateAcmerScore(Acmer acmer, Map<Integer, Contest> contests) {
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
        if (acmer.getSolvedProblems() < size) {
            int score = calculScore(problemsCount);
            acmer.setScore(score);
            acmer.setSolvedProblems(size);
            try {
                acmer.setSolvedProblemsDetails(new ObjectMapper().writeValueAsString(problemsCount));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            acmerRepository.save(acmer);
        }
    }

    public boolean createAcmer(String acmerData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(acmerData);
            String handle = rootNode.path("handle").asText();
            String password = rootNode.path("password").asText();
            String firstName = rootNode.path("firstName").asText();
            String lastName = rootNode.path("lastName").asText();
            String email = rootNode.path("email").asText();
            Optional<Acmer> acmerObj = acmerRepository.findById(handle);
            System.out.println(handle);
            if (acmerObj.isPresent()) {
                return false;
            }
            Acmer acmer = getAcmerInfosByHandle(handle);
            acmer.setFirstName(firstName);
            acmer.setLastName(lastName);
            acmer.setEmail(email);
            String salt = this.passwordEncryption.generateSalt();
            String encryptedPassword = this.passwordEncryption.getEncryptedPassword(password, salt);
            acmer.setPassword(encryptedPassword);
            acmer.setSalt(salt);
            acmer.setCountry("Tunisia");
            acmerRepository.save(acmer);
            Map<Integer, Contest> contests = acmerDAO.getAllAvailableContests();
            updateAcmerScore(acmer, contests);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void createAll(List<Acmer> acmerList) {
        Map<Integer, Contest> contests = acmerDAO.getAllAvailableContests();
        for (Acmer acmer : acmerList) {
            String handle = acmer.getHandle();
            String password = acmer.getPassword();
            acmer = getAcmerInfosByHandle(handle);
            acmer.setPassword(password);
            acmerRepository.save(acmer);
            updateAcmerScore(acmer, contests);
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

  /*  @Bean
    public SmartInitializingSingleton importProcessor() {
        return () -> {
            this.updateAcmersScore();
        };

    }*/

    public Acmer login(String acmerData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(acmerData);
            String handle = rootNode.path("handle").asText();
            String password = rootNode.path("password").asText();
            Acmer acmerfromDB = acmerRepository.findByHandle(handle);
            System.out.println("from db"+acmerfromDB.toString());
            if (acmerfromDB != null) {
                String encryptedPassword = passwordEncryption.getEncryptedPassword(password, acmerfromDB.getSalt());
                //System.out.println(encryptedPassword);
                //System.out.println(acmerfromDB.getPassword());
                if (encryptedPassword.equals(acmerfromDB.getPassword())) {
                    String token = jwtTokenProvider.createToken(handle, Collections.singletonList(acmerfromDB.getRole()));
                    acmerfromDB.setToken(token);
                    System.out.println("***"+acmerfromDB.toString());
                    return acmerfromDB;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Acmer();
    }
}

