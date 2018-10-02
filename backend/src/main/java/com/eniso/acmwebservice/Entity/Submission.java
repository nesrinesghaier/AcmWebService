package com.eniso.acmwebservice.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Submission {
    @JsonProperty
    private int id;
    @JsonProperty
    private int contestId;
    @JsonProperty
    private long creationTimeSeconds;
    @JsonProperty
    private long relativeTimeSeconds;
    @JsonProperty
    private Problem problem;
    @JsonProperty
    private Author author;
    @JsonProperty
    private String programmingLanguage;
    @JsonProperty
    private String verdict;
    @JsonProperty
    private String testset;
    @JsonProperty
    private int passedTestCount;
    @JsonProperty
    private int timeConsumedMillis;
    @JsonProperty
    private int memoryConsumedBytes;

    public Submission() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public long getCreationTimeSeconds() {
        return creationTimeSeconds;
    }

    public void setCreationTimeSeconds(long creationTimeSeconds) {
        this.creationTimeSeconds = creationTimeSeconds;
    }

    public long getRelativeTimeSeconds() {
        return relativeTimeSeconds;
    }

    public void setRelativeTimeSeconds(long relativeTimeSeconds) {
        this.relativeTimeSeconds = relativeTimeSeconds;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public String getTestset() {
        return testset;
    }

    public void setTestset(String testset) {
        this.testset = testset;
    }

    public int getPassedTestCount() {
        return passedTestCount;
    }

    public void setPassedTestCount(int passedTestCount) {
        this.passedTestCount = passedTestCount;
    }

    public int getTimeConsumedMillis() {
        return timeConsumedMillis;
    }

    public void setTimeConsumedMillis(int timeConsumedMillis) {
        this.timeConsumedMillis = timeConsumedMillis;
    }

    public int getMemoryConsumedBytes() {
        return memoryConsumedBytes;
    }

    public void setMemoryConsumedBytes(int memoryConsumedBytes) {
        this.memoryConsumedBytes = memoryConsumedBytes;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", contestId=" + contestId +
                ", creationTimeSeconds=" + creationTimeSeconds +
                ", relativeTimeSeconds=" + relativeTimeSeconds +
                ", problem=" + problem +
                ", author=" + author +
                ", programmingLanguage='" + programmingLanguage + '\'' +
                ", verdict='" + verdict + '\'' +
                ", testset='" + testset + '\'' +
                ", passedTestCount=" + passedTestCount +
                ", timeConsumedMillis=" + timeConsumedMillis +
                ", memoryConsumedBytes=" + memoryConsumedBytes +
                '}';
    }
}
