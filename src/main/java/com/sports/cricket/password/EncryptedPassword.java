package com.sports.cricket.password;

import java.io.Serializable;

public class EncryptedPassword implements Serializable {

    String encryptedPassword;

    String salt;


    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
