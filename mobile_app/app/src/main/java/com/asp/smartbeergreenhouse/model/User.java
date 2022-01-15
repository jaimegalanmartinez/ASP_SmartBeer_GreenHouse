package com.asp.smartbeergreenhouse.model;

import java.io.Serializable;

/**
 * User class
 * <p>Represents a user</p>
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class User implements Serializable {
    /**
     * Enum Type
     * <p>The user can be : Farmer or Brewery</p>
     */
    public enum Type  {Farmer, Brewery}

    private int id;
    /**
     * Represents the username
     */
    private String name;
    /**
     * Represents the user's password
     */
    private String password;
    /**
     * Represents the user's type
     */
    private Type type;

    /**
     * User class constructor
     * @param name username
     * @param password user's password
     * @param type user's type
     */
    public User (String name, String password, Type type){
        this.name = name;
        this.password = password;
        this.type = type;
    }

    /**
     * getName
     * @return the username
     */
    public String getName() {
        return name;
    }

    /**
     * getPassword
     * @return user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * getType
     * @return user's type (Farmer or Brewery)
     */
    public Type getType() {
        return type;
    }
}
