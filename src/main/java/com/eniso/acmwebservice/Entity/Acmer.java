package com.eniso.acmwebservice.Entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Acmer {
    @Nullable
    @Column(name = "lastName")
    private  String lastName;
    @Id
    private  String handle;
    @Column(name = "email")
    @Nullable
    private  String email;
    @Column(name = "firstName")
    @Nullable
    private  String firstName;
    @Column(name = "country")
    private  String country;
    @Column(name = "rank")
    private  String rank;
    @Column(name = "maxRank")
    private  String maxRank;
    @Column(name = "rating")
    private  int rating;
    @Column(name = "maxRating")
    private  int maxRating;
    @Column(name = "solvedProblems")
    private  int solvedProblems;
    @Enumerated(EnumType.STRING)
    private  Role role;


    public Acmer(String handle) throws IOException {
        Acmer acmer = getAcmerInfosByHandle(handle);
        this.lastName = acmer.getLastName();
        this.handle = handle;
        this.email = acmer.getEmail();
        this.firstName = acmer.getFirstName();
        this.country = acmer.getCountry();
        this.rank = acmer.getRank();
        this.maxRank = acmer.getMaxRank();
        this.rating = acmer.getRating();
        this.maxRating = acmer.getMaxRating();
        this.solvedProblems = getAcmerSolvedProblems(handle);
        this.role = acmer.getRole();
    }
    public Acmer(String handle, String email, String firstName, String lastName, String country, String rank, String maxRank, int rating, int maxRating, int solvedProblems, Role role) {
        this.lastName = lastName;
        this.handle = handle;
        this.email = email;
        this.firstName = firstName;
        this.country = country;
        this.rank = rank;
        this.maxRank = maxRank;
        this.rating = rating;
        this.maxRating = maxRating;
        this.solvedProblems = solvedProblems;
        this.role = role;
    }

    public Acmer getAcmerInfosByHandle(String handle) throws IOException {
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
        return acmer;
    }
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getMaxRank() {
        return maxRank;
    }

    public void setMaxRank(String maxRank) {
        this.maxRank = maxRank;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    public int getSolvedProblems() {
        return solvedProblems;
    }

    public void setSolvedProblems(int solvedProblems) {
        this.solvedProblems = solvedProblems;
    }

    public Acmer(){}

    @Override
    public String toString() {
        return "Acmer{" +
                "lastName='" + lastName + '\'' +
                ", handle='" + handle + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", country='" + country + '\'' +
                ", rank='" + rank + '\'' +
                ", maxRank='" + maxRank + '\'' +
                ", rating=" + rating +
                ", maxRating=" + maxRating +
                ", solvedProblems=" + solvedProblems +
                ", role=" + role +
                '}';
    }

    public int getAcmerSolvedProblems(String handle) {
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
            for (Map.Entry e : problemTypes.entrySet()) {
                sb.append(e.getKey()).append("   ").append(e.getValue()).append("<br>");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return set.size();
    }
}
