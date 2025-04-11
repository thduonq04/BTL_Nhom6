package vn.edu.tlu.cse.nhom6.ticketbookingapp.model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String phone_number;
    private String email;
    private String password;
    private String role;
    private String full_name;

    public User() {
    }
    public User(int id, String phone_number, String email, String password, String role, String full_name) {
        this.id = id;
        this.phone_number = phone_number;
        this.email = email;
        this.password = password;
        this.role = role;
        this.full_name = full_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
