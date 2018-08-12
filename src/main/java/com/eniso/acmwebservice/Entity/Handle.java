package com.eniso.acmwebservice.Entity;

public class Handle {
    private String handle;

    public Handle(String handle) {
        this.handle = handle;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    @Override
    public String toString() {
        return "Handle{" +
                "handle='" + handle + '\'' +
                '}';
    }
}
