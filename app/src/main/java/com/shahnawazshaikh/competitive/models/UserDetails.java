package com.shahnawazshaikh.competitive.models;

public class UserDetails {
    private String name;
    private String email;
    private String password;
    private String institute;

    public UserDetails(String email, String password) {
        this.email=email;
        this.password=password;
    }

    public String getName() {
        return name;
    }

    public UserDetails(String name, String email, String password, String institute) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.institute = institute;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }
}
