package com.eniso.acmwebservice.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SubmissionWrapper {
    @JsonProperty
    private String status;
    @JsonProperty
    private List<Submission> result;


   /* public SubmissionWrapper(String status, List<Submission> result) {
        this.status = status;
        this.result = result;
    }*/

    public List<Submission> getResult() {
        return result;
    }

    public void setResult(List<Submission> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
