package com.eniso.acmwebservice.Controller;

import com.eniso.acmwebservice.Entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.eniso.acmwebservice.Dao.AcmerRepository;
import com.eniso.acmwebservice.Dao.StorageService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.fasterxml.jackson.databind.type.TypeFactory;

import javax.persistence.EntityManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/acmers")
@Controller
public class AcmerController {

    @Autowired
    StorageService storageService;
    EntityManager entityManager;
    public static final Logger logger = LoggerFactory.getLogger(AcmerController.class);
    List<String> files = new ArrayList<String>();
    @Autowired
    private AcmerRepository acmerRepository;
    Map<Integer, Contest> contests = null;

    @GetMapping(value = "")
    public Collection<Acmer> getAllAcmer() {
        List<Acmer> acmerList = new ArrayList<>();
        acmerRepository.findAll().forEach(acmerList::add);
        return acmerList;
    }

    @PostMapping("/createAll")
    public void UploadFile(MultipartHttpServletRequest request) throws IOException {
        getAllAvailableContests();
        Iterator<String> itr = request.getFileNames();
        MultipartFile file = request.getFile(itr.next());
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory factory = mapper.getTypeFactory();
        CollectionType listType = factory.constructCollectionType(List.class, Acmer.class);
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
            String handle = acmer.getHandle();
            String password = acmer.getPassword();

            if (acmer != null) {
                acmer = getAcmerInfosByHandle(handle);
                acmer = updateAcmerSolvedProblemsAndScore(acmer);
                acmer.setPassword(password);
                acmerRepository.save(acmer);

            }
        }
    }

    @PostMapping(value = "/create")
    public void createAcmer(@RequestBody String loginString) throws IOException {
        getAllAvailableContests();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(loginString);
        String handle = rootNode.path("handle").asText();
        String password = rootNode.path("password").asText();
        String firstName = rootNode.path("firstName").asText();
        String lastName = rootNode.path("lastName").asText();
        String email = rootNode.path("email").asText();
        Optional<Acmer> acmerData = acmerRepository.findById(handle);
        if (acmerData.isPresent()) {
            Acmer acmerDB = acmerData.get();
            acmerDB.setEmail(email);
            acmerDB.setCountry("Tunisia");
            acmerDB.setFirstName(firstName);
            acmerDB.setLastName(lastName);
            acmerDB = updateAcmerSolvedProblemsAndScore(acmerDB);
            acmerDB.setPassword(password);
            acmerRepository.save(acmerDB);
            return;
        }
        Acmer acmer = getAcmerInfosByHandle(handle);
        acmer = updateAcmerSolvedProblemsAndScore(acmer);
        if (acmer.getEmail() == null && !email.isEmpty())
            acmer.setEmail(email);
        if (acmer.getFirstName().isEmpty() && !firstName.isEmpty())
            acmer.setFirstName(firstName);
        if (acmer.getLastName().isEmpty() && !lastName.isEmpty())
            acmer.setLastName(lastName);
        acmer.setPassword(password);
        acmer.setCountry("Tunisia");
        //System.out.println(acmer.toString() + " after update ");
        if (acmer != null)
            acmerRepository.save(acmer);
    }

    @DeleteMapping("/{handle:.+}")
    public Collection<Acmer> deleteAcmer(@PathVariable("handle") String handle) {
        Acmer acmer = acmerRepository.findByHandle(handle);
        if (acmer != null) {
            acmerRepository.delete(acmer);
        }
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

    @GetMapping(value = "/{handle:.+}")
    public Acmer findByHandle(@PathVariable String handle) throws IOException {
        Acmer acmer = acmerRepository.findByHandle(handle);
        if (acmer == null) {
            return getAcmerInfosByHandle(handle);
        }
        return acmer;
    }

    @PutMapping(value = "/{handle:.+}")
    public void updateAcmer(@PathVariable String handle, @RequestBody Acmer acmer) {
        acmerRepository.save(acmer);
        Optional<Acmer> acmerData = acmerRepository.findById(handle);
        if (acmerData.isPresent()) {
            Acmer acmerDB = acmerData.get();
            acmerDB.setEmail(acmer.getEmail());
            acmerDB.setCountry(acmer.getCountry());
            acmerDB.setFirstName(acmer.getFirstName());
            acmerDB.setLastName(acmer.getLastName());
            acmerDB.setRole(acmer.getRole());
        }
    }

    public Acmer getAcmerInfosByHandle(String handle) throws IOException {
        if (handle.isEmpty())
            return null;
        Acmer acmer = new Acmer();
        StringBuilder sb = new StringBuilder();
        org.codehaus.jackson.map.ObjectMapper objectMapper = new org.codehaus.jackson.map.ObjectMapper();
        AcmerWrapper result = objectMapper.readValue(new URL("http://codeforces.com/api/user.info?handles=" + handle), AcmerWrapper.class);
        acmer = result.getResult().get(0);

        if (acmer.getHandle().equals("bacali") || acmer.getHandle().equals("myob-_-")) {
            acmer.setRole(Role.ADMIN);
        } else {
            acmer.setRole(Role.USER);
        }
        return acmer;
    }

    @RequestMapping("/login")
    public Principal user(Principal principal) {
        logger.info("user logged " + principal);
        return principal;
    }


    public Acmer updateAcmerSolvedProblemsAndScore(Acmer acmer) {
        Set<String> set = new HashSet();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            URL url = new URL("http://codeforces.com/api/user.status?handle=" + acmer.getHandle());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            JsonSubResult result = objectMapper.readValue(url, JsonSubResult.class);
            Map<String, Integer> problemsCount = new HashMap<>();
            problemsCount.put("A", 0);
            problemsCount.put("B", 0);
            problemsCount.put("C", 0);
            problemsCount.put("D", 0);
            problemsCount.put("E", 0);
            problemsCount.put("F", 0);
            problemsCount.put("G", 0);
            problemsCount.put("H", 0);

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
            Acmer acmerDB = acmerRepository.findByHandle(acmer.getHandle());
            if (acmerDB!=null) {
                if (acmerDB.getSolvedProblems() < size) {
                    System.out.println("acmer changed " + acmer.toString());
                    int score = calculScore(problemsCount);
                    acmerDB.setScore(score);
                    acmerDB.setSolvedProblems(size);
                    return acmerDB;
                }
            }
            acmer = getAcmerInfosByHandle(acmer.getHandle());
            acmer.setScore(calculScore(problemsCount));
            acmer.setSolvedProblems(size);
            return acmer;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return acmer;
    }

    public void getAllAvailableContests() {
        if (contests == null) {
            HttpURLConnection urlConnection;
            URL url;
            String jsonString;
            try {
                url = new URL("http://codeforces.com/api/contest.list?gym=false");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                contests = new TreeMap<>();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(url);
                JsonNode constestNode = rootNode.path("result");
                int Listsize = constestNode.size();
                for (int i = 0; i < Listsize; i++) {
                    JsonNode obj = constestNode.get(i);
                    Contest contest = new Contest(obj.get("id").asInt(),
                            obj.get("name").toString(),
                            obj.get("type").toString(),
                            obj.get("phase").toString(),
                            obj.get("frozen").asBoolean(),
                            obj.get("durationSeconds").asLong(),
                            obj.get("startTimeSeconds").asLong(),
                            obj.get("relativeTimeSeconds").asLong());
                    contests.put(obj.get("id").asInt(), contest);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int calculScore(Map<String, Integer> problemsCount) {
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
        //System.out.println("score  = "+score);
        return score;
    }
}

