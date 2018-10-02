package com.eniso.acmwebservice.Entity;

public class Contest {
private int id;
private String name;
private String type;
private String phase;
private boolean frozen;
private long durationSeconds;
private long startTimeSeconds;
private long relativeTimeSeconds;

    public Contest(int id, String name, String type, String phase, boolean frozen, long durationSeconds, long startTimeSeconds, long relativeTimeSeconds) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.phase = phase;
        this.frozen = frozen;
        this.durationSeconds = durationSeconds;
        this.startTimeSeconds = startTimeSeconds;
        this.relativeTimeSeconds = relativeTimeSeconds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public long getStartTimeSeconds() {
        return startTimeSeconds;
    }

    public void setStartTimeSeconds(long startTimeSeconds) {
        this.startTimeSeconds = startTimeSeconds;
    }

    public long getRelativeTimeSeconds() {
        return relativeTimeSeconds;
    }

    public void setRelativeTimeSeconds(long relativeTimeSeconds) {
        this.relativeTimeSeconds = relativeTimeSeconds;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", phase='" + phase + '\'' +
                ", frozen=" + frozen +
                ", durationSeconds=" + durationSeconds +
                ", startTimeSeconds=" + startTimeSeconds +
                ", relativeTimeSeconds=" + relativeTimeSeconds +
                '}';
    }
}

