package ru.stqa.training.selenium.model;

public class Account {
    public String email;
    public String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Account withEmail(String email) {
        this.email = email;
        return this;
    }

    public Account withPassword(String password) {
        this.password = password;
        return this;
    }
}
