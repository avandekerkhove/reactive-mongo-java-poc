package com.alex.poc.reactive.mongo.customer;

public class Customer {

    private String id;
    private String login;
    private String name;
    private int age;
    
    // need this
    public Customer() {}
    
    public Customer(String login, String name, int age) {
        this.id = login;
        this.login = login;
        this.name = name;
        this.age = age;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
}
