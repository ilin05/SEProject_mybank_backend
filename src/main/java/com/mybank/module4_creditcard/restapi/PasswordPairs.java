package com.mybank.module4_creditcard.restapi;

public class PasswordPairs {
    String newPassword, oldPassword;

    public void setNewPassword(String pwd) {
        newPassword = pwd;
    }

    public void setOldPassword(String pwd) {
        oldPassword = pwd;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }
}
