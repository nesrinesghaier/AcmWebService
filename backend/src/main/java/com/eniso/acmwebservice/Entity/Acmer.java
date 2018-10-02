package com.eniso.acmwebservice.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Acmer implements Serializable, UserDetails {
    @Id
    @Column(name = "handle")
    private String handle;
    @Column(name = "firstName")
    @Nullable
    private String firstName;
    @Nullable
    @Column(name = "lastName")
    private String lastName;
    @Nullable
    @Column(name = "email")
    private String email;
    @Nullable
    @Column(name = "country")
    private String country;
    @Nullable
    @Column(name = "rank")
    private String rank;
    @Nullable
    @Column(name = "maxRank")
    private String maxRank;
    @Nullable
    @Column(name = "rating")
    private int rating;
    @Nullable
    @Column(name = "maxRating")
    private int maxRating;
    @Nullable
    @Column(name = "solvedProblems")
    private int solvedProblems;
    @Nullable
    @Column(name = "score")
    private int score;
    @Column(name = "password", unique = true)
    private String password;
    @Column(name = "token", unique = true)
    @Nullable
    private String token;
    @Column(name = "salt", unique = true)
    private String salt;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Nullable
    @Column(name = "solvedProblemsDetails")
    private String solvedProblemsDetails;

    public Acmer(String handle, String email, String firstName, String lastName, String country, String rank, String maxRank, int rating, int maxRating, int solvedProblems, String password, Role role, String solvedProblemsDetails) {
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
        this.password = password;
        this.role = role;
        this.solvedProblemsDetails = solvedProblemsDetails;
    }

    public Acmer() {
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return getHandle();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public String getSolvedProblemsDetails() {
        return solvedProblemsDetails;
    }

    public void setSolvedProblemsDetails(String solvedProblemsDetails) {
        this.solvedProblemsDetails = solvedProblemsDetails;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority auth = () -> getRole().getAuthority();
        return Collections.singletonList(auth);
    }

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
                ", score=" + score +
                ", password=" + password +
                ", salt= "+salt+
                ", token= "+token+
                ", role=" + role +
                '}';
    }

}
