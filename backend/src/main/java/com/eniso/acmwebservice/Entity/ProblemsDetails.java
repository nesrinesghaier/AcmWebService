package com.eniso.acmwebservice.Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TBL_PROBLEM_DETAILS")
public class ProblemsDetails implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    private String problemIndex;
    private int solvedCount;

    public ProblemsDetails() {

    }

    public ProblemsDetails(String problemIndex, int solvedCount) {
        this.problemIndex = problemIndex;
        this.solvedCount = solvedCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProblemIndex() {
        return problemIndex;
    }

    public void setProblemIndex(String problemIndex) {
        this.problemIndex = problemIndex;
    }

    public int getSolvedCount() {
        return solvedCount;
    }

    public void setSolvedCount(int solvedCount) {
        this.solvedCount = solvedCount;
    }

    @Override
    public String toString() {
        return "ProblemsDetails{" +
                "problemIndex='" + problemIndex + '\'' +
                ", solvedCount=" + solvedCount +
                '}';
    }
}
