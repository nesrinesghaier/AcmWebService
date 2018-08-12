package com.eniso.acmwebservice.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.util.Map;

public class Author {
    @JsonProperty 
    private int contestId;
    @JsonProperty
    private Map<String, String>[] members;
    @JsonProperty
    private String participantType;
    @JsonProperty
    @Nullable
    private int teamId;
    @JsonProperty
    private String teamName;
    @JsonProperty
    private boolean ghost;
    @JsonProperty
    private int room;
    @JsonProperty
    private long startTimeSeconds;


    /*public Author(int contestId, Map<String, String>[] members, String participantType, boolean ghost, long startTimeSeconds) {
        this.contestId = contestId;
        this.members = members;
        this.participantType = participantType;
        this.ghost = ghost;
        this.startTimeSeconds = startTimeSeconds;
    }*/

  

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public Map<String, String>[] getMembers() {
        return members;
    }

    public void setMembers(Map<String, String>[] members) {
        this.members = members;
    }

    public String getParticipantType() {
        return participantType;
    }

    public void setParticipantType(String participantType) {
        this.participantType = participantType;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public boolean isGhost() {
        return ghost;
    }

    public void setGhost(boolean ghost) {
        this.ghost = ghost;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public long getStartTimeSeconds() {
        return startTimeSeconds;
    }

    public void setStartTimeSeconds(long startTimeSeconds) {
        this.startTimeSeconds = startTimeSeconds;
    }

    @Override
    public String toString() {
        return "Author{" + "contestId=" + contestId + ", members=" + members + ", participantType=" + participantType + ", teamId=" + teamId + ", ghost=" + ghost + ", room=" + room + ", startTimeSeconds=" + startTimeSeconds + '}';
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
}
