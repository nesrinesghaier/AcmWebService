package com.eniso.acmwebservice.Entity;

import javax.persistence.*;

@Entity
public class Acmer {
    @Id
    private  String handle;
    @Column(name = "email")
    private  String email;
    @Column(name = "firstName")
    private  String firstName;
    @Column(name = "lasttName")
    private  String lasttName;
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
    @Column(name = "problemSolved")
    private  int problemSolved;
    @Enumerated(EnumType.STRING)
    private  Role role;


    public Acmer(String handle, String email, String firstName, String lasttName, String country, String rank, String maxRank, int rating, int maxRating, int problemSolved,Role role) {
        this.handle = handle;
        this.email = email;
        this.firstName = firstName;
        this.lasttName = lasttName;
        this.country = country;
        this.rank = rank;
        this.maxRank = maxRank;
        this.rating = rating;
        this.maxRating = maxRating;
        this.problemSolved = problemSolved;
        this.role = role;
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

    public String getLasttName() {
        return lasttName;
    }

    public void setLasttName(String lasttName) {
        this.lasttName = lasttName;
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

    public int getProblemSolved() {
        return problemSolved;
    }

    public void setProblemSolved(int problemSolved) {
        this.problemSolved = problemSolved;
    }

    public Acmer(){}


}
