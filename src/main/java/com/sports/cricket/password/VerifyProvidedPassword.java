package com.sports.cricket.password;

import com.sports.cricket.model.Register;

import java.io.Serializable;

public class VerifyProvidedPassword implements Serializable {

   /* public static void main(String[] args)
    {
        // User provided password to validate
        String providedPassword = "myPassword123";

        // Encrypted and Base64 encoded password read from database
        String securePassword = "HhaNvzTsVYwS/x/zbYXlLOE3ETMXQgllqrDaJY9PD/U=";

        // Salt value stored in database
        String salt = "EqdmPh53c9x33EygXpTpcoJvc4VXLK";

        boolean passwordMatch = PasswordUtils.verifyUserPassword(providedPassword, securePassword, salt);

        if(passwordMatch)
        {
            System.out.println("Provided user password " + providedPassword + " is correct.");
        } else {
            System.out.println("Provided password is incorrect");
        }
    }*/

    public static boolean decryptPassword(String password, Register register){

        return PasswordUtils.verifyUserPassword(password, register.getEncryptedPass(), register.getSaltKey());
    }

}
