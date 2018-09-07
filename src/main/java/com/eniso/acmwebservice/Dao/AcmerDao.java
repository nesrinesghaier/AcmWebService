package com.eniso.acmwebservice.Dao;

import com.fasterxml.jackson.databind.DeserializationFeature;

import com.eniso.acmwebservice.Entity.*;
import com.eniso.acmwebservice.Entity.JsonResult;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.Map.Entry;

import com.eniso.acmwebservice.Entity.Role;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
@Repository

public class AcmerDao {

    private static String[] handles;
    private static Map<String, Acmer> AllAcmers = new HashMap() {
        {
            put("bacali", new Acmer("bacali", "nasreddine.bacali@gmai.com", "nasreddine", "bac ali", "Tunisia", "1500", "1700", 25, 60, 520, Role.ADMIN));
            put("myob-_-", new Acmer("myob-_-", "nasreddine.bacali@gmai.com", "nesrine", "sghaier", "Tunisia", "1300", "1500", 22, 40, 220, Role.ADMIN));
        }
    };

    static {
        handles = new String[]{"myob-_-", "bacali"};
    }


    public Collection<Acmer> getAllAcmers() {
        return this.AllAcmers.values();
    }

    public Acmer getAcmerByHandle(String handle) {
        return this.AllAcmers.get(handle);
    }

    public void deleteAcmerByHandle(String handle) {
        this.AllAcmers.remove(handle);
    }

    public void addAcmerByHandle(String handle) throws IOException {
        Acmer acmer = new Acmer();
        StringBuilder sb = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        String string = new String("");
        AcmerWrapper result = objectMapper.readValue(new URL("http://codeforces.com/api/user.info?handles=" + handle), AcmerWrapper.class);
        acmer = result.getResult().get(0);
        if (acmer.getHandle().equals("bacali") || acmer.getHandle().equals("myob-_-")) {
            acmer.setRole(Role.ADMIN);
        } else {
            acmer.setRole(Role.USER);
        }
        this.AllAcmers.put(acmer.getHandle(),acmer);
    }

    /*public String getAcmerSolvedProblems(String handle) {
        Set<String> set = new HashSet();
        Map<String, Integer> problemTypes = new TreeMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder sb = new StringBuilder();
        try {
            JsonResult result = objectMapper.readValue(new URL("http://codeforces.com/api/user.status?handle=" + handle), JsonResult.class);
            int problemSolved = 0;

            for (int i = 0; i < result.getResult().size(); i++) {
                if (result.getResult().get(i).getVerdict().equals("OK")) {
                    Problem problem = result.getResult().get(i).getProblem();
                    if (!set.contains(problem.getName())) {
                        if (!problemTypes.containsKey(problem.getIndex())) {
                            problemTypes.put(problem.getIndex(), 0);
                        }
                        problemTypes.put(problem.getIndex(), problemTypes.get(problem.getIndex()) + 1);
                    }
                    set.add(problem.getName());

                }
            }
            sb.append(set.size()).append("<br>");
            //System.out.println(set.size());
            //objectMapper.writeValueAsString(set.size());
            for (Entry e : problemTypes.entrySet()) {
                sb.append(e.getKey()).append("   ").append(e.getValue()).append("<br>");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }*/

}
