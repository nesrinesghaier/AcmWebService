package com.eniso.acmwebservice.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JsonResult  {
    @JsonProperty
    private String status;
    @JsonProperty
    private List<Result> result;


   /* public JsonResult(String status, List<Result> result) {
        this.status = status;
        this.result = result;
    }*/

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
