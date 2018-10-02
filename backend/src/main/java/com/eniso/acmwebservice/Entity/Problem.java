package com.eniso.acmwebservice.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Problem {
    @JsonProperty
    private int contestId;
    @JsonProperty
    private String index;
    @JsonProperty
    private String name;
    @JsonProperty
    private String type;
    @JsonProperty
    private int points;
    @JsonProperty
    private String problemsetName;
    @JsonProperty
    private String []tags;

    public Problem() {
    }

    public String getProblemsetName() {
        return problemsetName;
    }

    public void setProblemsetName(String problemsetName) {
        this.problemsetName = problemsetName;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Problem{" + "contestId=" + contestId + ", index=" + index + ", name=" + name + ", type=" + type + ", points=" + points + ", problemsetName=" + problemsetName + ", tags=" + tags + '}';
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Problem other = (Problem) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
}
