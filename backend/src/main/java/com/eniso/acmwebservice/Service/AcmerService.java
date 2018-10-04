package com.eniso.acmwebservice.Service;

import com.eniso.acmwebservice.Dao.AcmerDAO;
import com.eniso.acmwebservice.Dao.AcmerRepository;
import com.eniso.acmwebservice.Entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Service(value = "acmerService")
@Transactional
public class AcmerService implements UserDetailsService {

    @Autowired
    AcmerRepository acmerRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private AcmerDAO acmerDAO = new AcmerDAO();

    public Acmer findByHandle(String handle) {
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
            if (acmerObj.isPresent()) {
                return false;
            }
            Acmer acmer = getAcmerInfosByHandle(handle);
            acmer.setFirstName(firstName);
            acmer.setLastName(lastName);
            acmer.setEmail(email);
            String encryptedPassword = this.bCryptPasswordEncoder.encode(password);
            acmer.setPassword(encryptedPassword);
            acmer.setCountry("Tunisia");
            acmerRepository.save(acmer);
            //Map<Integer, Contest> contests = acmerDAO.getAllAvailableContests();
            //updateAcmerScore(acmer, contests);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean createAll(MultipartFile file) {
        try {
            InputStreamReader reader = new InputStreamReader(file.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            while (br.ready()) {
                sb.append(br.readLine());
            }
            ObjectMapper mapper = new ObjectMapper();
            TypeFactory factory = mapper.getTypeFactory();
            CollectionType listType = factory.constructCollectionType(List.class, Acmer.class);
            List<Acmer> acmerList = mapper.readValue(sb.toString(), listType);

            Map<Integer, Contest> contests = acmerDAO.getAllAvailableContests();
            for (Acmer acmer : acmerList) {
                String handle = acmer.getHandle();
                String password = acmer.getPassword();
                acmer = getAcmerInfosByHandle(handle);
                acmer.setPassword(password);
                acmerRepository.save(acmer);
                updateAcmerScore(acmer, contests);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Acmer acmer = acmerRepository.findByHandle(username);
        if (acmer == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(acmer.getUsername(), acmer.getPassword(), acmer.getAuthorities());
    }

    public void populateAdmins() {
        if (acmerRepository.findByHandle("bacali") == null) {
            Acmer acmer = getAcmerInfosByHandle("bacali");
            acmer.setPassword(this.bCryptPasswordEncoder.encode("95253834"));
            acmerRepository.save(acmer);
        }
        if (acmerRepository.findByHandle("myob-_-") == null) {
            Acmer acmer = getAcmerInfosByHandle("myob-_-");
            acmer.setFirstName("Nesrine");
            acmer.setLastName("Sghaier");
            acmer.setEmail("nesrinesghaier10@gmail.com");
            acmer.setPassword(this.bCryptPasswordEncoder.encode("50609713"));
            acmerRepository.save(acmer);
        }
    }
}

