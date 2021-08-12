package com.example.recyclerviewproject;

public class Model {

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    String hint;

    public Model(String hint, String value) {
        this.hint = hint;
        this.value = value;
    }

    String value;
}
