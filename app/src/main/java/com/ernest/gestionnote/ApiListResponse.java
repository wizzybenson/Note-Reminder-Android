package com.ernest.gestionnote;

import java.util.List;

public class ApiListResponse {
    private int code;
    private String message;
    private List<NoteData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NoteData> getData() {
        return data;
    }

    public void setData(List<NoteData> data) {
        this.data = data;
    }
}


