package com.asp.smartbeergreenhouse.model;

public class User {
    private int id;
    private String name;
    private String password;

    public User (String name, String password){
        this.name = name;
        this.password = password;
    }
    public String getName() {
        return name;
    }
}
