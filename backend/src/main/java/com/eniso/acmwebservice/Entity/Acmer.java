package com.eniso.acmwebservice.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "TBL_ACMERS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Acmer implements Serializable, UserDetails {
    @Id
    private String handle;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String email;
    @Nullable
    private String country;
    @Nullable
    private String rank;
    @Nullable
    private String maxRank;
    private int rating;
    private int maxRating;
    private int solvedProblems;
    private int score;
    @Nullable
    @Column(unique = true)
    private String token;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(fetch = FetchType.EAGER)
    private Collection<ProblemsDetails> solvedProblemsDetails = new ArrayList<>();

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

    public Collection<ProblemsDetails> getSolvedProblemsDetails() {
        return solvedProblemsDetails;
    }

    public void setSolvedProblemsDetails(Collection<ProblemsDetails> solvedProblemsDetails) {
        this.solvedProblemsDetails = solvedProblemsDetails;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority auth = () -> getRole().getAuthority();
        return Collections.singletonList(auth);
    }

    @Override
    public String toString() {
        return "Acmer{" +
                "handle='" + handle + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", rank='" + rank + '\'' +
                ", maxRank='" + maxRank + '\'' +
                ", rating=" + rating +
                ", maxRating=" + maxRating +
                ", solvedProblems=" + solvedProblems +
                ", score=" + score +
                ", token='" + token + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", solvedProblemsDetails='" + solvedProblemsDetails + '\'' +
                '}';
    }
}
