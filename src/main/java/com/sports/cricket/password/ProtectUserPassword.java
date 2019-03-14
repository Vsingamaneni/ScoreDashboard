package com.sports.cricket.password;

import java.io.Serializable;

public class ProtectUserPassword implements Serializable {
    /*public static void main(String[] args)
    {
        String myPassword = "myPassword123";

        // Generate Salt. The generated value can be stored in DB.
        String salt = PasswordUtils.getSalt(30);

        // Protect user's password. The generated value can be stored in DB.
        String mySecurePassword = PasswordUtils.generateSecurePassword(myPassword, salt);

        // Print out protected password
        System.out.println("My secure password = " + mySecurePassword);
        System.out.println("Salt value = " + salt);
    }*/

    public static EncryptedPassword encryptPassword(String password){

        EncryptedPassword encryptedPassword = new EncryptedPassword();

        String salt = PasswordUtils.getSalt(30);

        encryptedPassword.setSalt(salt);
        encryptedPassword.setEncryptedPassword(PasswordUtils.generateSecurePassword(password, salt));

        return encryptedPassword;
    }

}
