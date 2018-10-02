package com.eniso.acmwebservice.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AcmerWrapper {
    @JsonProperty
    private String status;
    @JsonProperty
    private List<Acmer> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Acmer> getResult() {
        return result;
    }

    public void setResult(List<Acmer> result) {
        this.result = result;
    }
}
