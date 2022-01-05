package com.asp.smartbeergreenhouse.model;

import java.io.Serializable;

public class User implements Serializable {

    public enum Type  {Farmer, Brewery}

    private int id;
    private String name;
    private String password;
    private Type type;

    public User (String name, String password, Type type){
        this.name = name;
        this.password = password;
        this.type = type;
    }
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Type getType() {
        return type;
    }
}
