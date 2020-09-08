package dev.jundana.loginandregistrationmultiuser.model;

public class Users {
    private String email, username, password, status;
    private int id;

    public Users(int id, String username, String email, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}