package com.mybank.module4_creditcard.entity.role;

public class Auditor {
    private String auditorId;
    private String name;
    private String password;
    private String phone;
    private Authority auth;

    public Auditor() {
    }

    public Auditor(String name, String password, String phone, Authority auth) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.auth = auth;
    }

    public Authority getAuth() {
        return auth;
    }

    public void setAuth(Authority auth) {
        this.auth = auth;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
