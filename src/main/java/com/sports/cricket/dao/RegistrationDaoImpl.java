package com.sports.cricket.dao;

import com.sports.cricket.model.Register;
import com.sports.cricket.model.Restrictions;
import com.sports.cricket.model.UserLogin;
import com.sports.cricket.password.EncryptedPassword;
import com.sports.cricket.password.ProtectUserPassword;
import com.sports.cricket.password.VerifyProvidedPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationDaoImpl implements RegistrationDao {


    DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean registerUser(Register registration) {

        System.out.println("Inside Member Registration");

        if(registration != null
                && null != registration.getPassword()){
            EncryptedPassword encryptedPassword = ProtectUserPassword.encryptPassword(registration.getPassword());
            registration.setEncryptedPass(encryptedPassword.getEncryptedPassword());
            registration.setSaltKey(encryptedPassword.getSalt());
            registration.setRegisteredTime(getTime().toString());
            registration.setIsActive("N");
            registration.setRole("member");
            registration.setIsAdminActivated("N");
        }

        String sql = "INSERT INTO REGISTER(event, title, fname, lname, registeredTime, emailId, gender, mobile, country, encryptedPass, saltKey, securityQuestion, securityAnswer, securityKey, isActive, role, isAdminActivated) "
                    + "VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, registration.getEvent());
            ps.setString(2, registration.getTitle());
            ps.setString(3, registration.getfName());
            ps.setString(4, registration.getlName());
            ps.setString(5, registration.getRegisteredTime());
            ps.setString(6, registration.getEmailId());
            ps.setString(7, registration.getGender());
            ps.setString(8, registration.getMobile());
            ps.setString(9, registration.getCountry());
            ps.setString(10, registration.getEncryptedPass());
            ps.setString(11, registration.getSaltKey());
            ps.setString(12, registration.getSecurityQuestion());
            ps.setString(13, registration.getSecurityAnswer());
            ps.setString(14, registration.getSecurity());
            ps.setString(15, registration.getIsActive());
            ps.setString(16, registration.getRole());
            ps.setString(17, registration.getIsAdminActivated());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }


        System.out.println("Member Update Done");

        return true;
    }

    @Override
    public UserLogin loginUser(UserLogin userLogin) {
        System.out.println("Inside Member Login");

        Map<String, Object> params = new HashMap<>();
        params.put("email", userLogin.getEmail());

        String selectSQL = "SELECT * FROM REGISTER where emailId = '" + userLogin.getEmail() + "'";

        Connection conn = null;
        Statement statement;
        ResultSet resultSet;
        Register register =null;

        try {
            conn = dataSource.getConnection();
            conn.prepareStatement(selectSQL);

            statement = conn.createStatement();
            resultSet = statement.executeQuery(selectSQL);

            while(resultSet.next()){
                register = new Register();
                register.setfName(resultSet.getString("fName"));
                register.setlName(resultSet.getString("lName"));
                register.setMemberId(resultSet.getInt("memberId"));
                register.setRegisteredTime(resultSet.getString("registeredTime"));
                register.setIsActive(resultSet.getString("isActive"));
                register.setRole(resultSet.getString("role"));
                register.setEncryptedPass(resultSet.getString("encryptedPass"));
                register.setSaltKey(resultSet.getString("saltKey"));
                register.setIsAdminActivated(resultSet.getString("isAdminActivated"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }


        if(null != register
            && null != register.getEncryptedPass() && null != register.getSaltKey()){
               if(!VerifyProvidedPassword.decryptPassword(userLogin.getPassword(), register)){
                   System.out.println("Passwords mismatch");
               }else{
                   System.out.println("Correct Password");
                   userLogin.setFirstName(register.getfName());
                   userLogin.setLastName(register.getlName());
                   userLogin.setMemberId(register.getMemberId());
                   userLogin.setLoginSuccess(true);
                   userLogin.setIsActive(register.getIsActive());
                   userLogin.setRole(register.getRole());
                   userLogin.setRegisteredTime(register.getRegisteredTime());
                   userLogin.setIsAdminActivated(register.getIsAdminActivated());
               }
           }
        return userLogin;
    }

    @Override
    public Register getUser(String emailId) {
        System.out.println("Inside retrieving User");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", emailId);

        String getUser = "SELECT * FROM REGISTER where emailId = '" + emailId + "'";

        Connection conn = null;
        Statement statement;
        ResultSet resultSet;
        Register register =null;

        try {
            conn = dataSource.getConnection();
            conn.prepareStatement(getUser);

            statement = conn.createStatement();
            resultSet = statement.executeQuery(getUser);

            while(resultSet.next()){
                register = new Register();
                register.setfName(resultSet.getString("fName"));
                register.setlName(resultSet.getString("lName"));
                register.setEmailId(resultSet.getString("emailId"));
                register.setMemberId(resultSet.getInt("memberId"));
                register.setIsActive(resultSet.getString("isActive"));
                register.setRegisteredTime(resultSet.getString("registeredTime"));
                register.setRole(resultSet.getString("role"));
                register.setEncryptedPass(resultSet.getString("encryptedPass"));
                register.setSaltKey(resultSet.getString("saltKey"));
                register.setSecurityQuestion(resultSet.getString("securityQuestion"));
                register.setSecurityAnswer(resultSet.getString("securityAnswer"));
                register.setIsAdminActivated(resultSet.getString("isAdminActivated"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(null != register
                && null != register.getSecurityAnswer()
                && null != register.getSecurityQuestion()){
            System.out.println("Retrieved user successfully");
        }

        return register;
    }

    @Override
    public boolean updatePassword(Register register) {

        if(register != null
                && null != register.getPassword()){
            EncryptedPassword encryptedPassword = ProtectUserPassword.encryptPassword(register.getPassword());
            register.setEncryptedPass(encryptedPassword.getEncryptedPassword());
            register.setSaltKey(encryptedPassword.getSalt());
        }

        String sql = "UPDATE REGISTER SET encryptedPass = '" +register.getEncryptedPass() +"', saltKey = '" +register.getSaltKey() +"' where emailId = '" + register.getEmailId() +"'";

        int row = jdbcTemplate.update(sql);

        return (row ==1) ? true : false;
    }

    @Override
    public List<Register> getAllUsers() {

        String sql = "Select * from REGISTER";

        List<Register> register = jdbcTemplate.query(sql,new BeanPropertyRowMapper(Register.class));

        return register;
    }

    @Override
    public List<Restrictions> getRestrictions() {
        String sql = "Select * from RESTRICTIONS";

        List<Restrictions> restrictions = jdbcTemplate.query(sql,new BeanPropertyRowMapper(Restrictions.class));

        return restrictions;
    }

    @Override
    public boolean optOutUser(Integer memberId, String optOut) {

        String sql = "UPDATE REGISTER SET isActive = '" + optOut + "' where memberId = " + memberId;

        int row = jdbcTemplate.update(sql);

        return (row ==1) ? true : false;
    }

    private LocalDateTime getTime(){
        return java.time.LocalDateTime.now();
    }

}
