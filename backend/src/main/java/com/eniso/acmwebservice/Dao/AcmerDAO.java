package com.eniso.acmwebservice.Dao;

import com.eniso.acmwebservice.Entity.Acmer;
import com.eniso.acmwebservice.Entity.AcmerWrapper;
import com.eniso.acmwebservice.Entity.Contest;
import com.eniso.acmwebservice.Entity.SubmissionWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AcmerDAO {

    public SubmissionWrapper getSubmissionResult(Acmer acmer) {
        SubmissionWrapper result = new SubmissionWrapper();
        try {
            URL url = new URL("http://codeforces.com/api/user.status?handle=" + acmer.getHandle());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            ObjectMapper objectMapper = new ObjectMapper();
            result = objectMapper.readValue(url, SubmissionWrapper.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Acmer getJsonResult(String handle) {
        Acmer acmer = new Acmer();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AcmerWrapper result = objectMapper.readValue(new URL("http://codeforces.com/api/user.info?handles=" + handle), AcmerWrapper.class);
            acmer = result.getResult().get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return acmer;
    }

    public Map<Integer, Contest> getAllAvailableContests() {
        Map<Integer, Contest> contests = new HashMap<>();
        HttpURLConnection urlConnection;
        URL url;
        try {
            url = new URL("http://codeforces.com/api/contest.list?gym=false");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(url);
            JsonNode constestNode = rootNode.path("result");
            int listSize = constestNode.size();
            for (int i = 0; i < listSize; i++) {
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
        return contests;
    }
}
