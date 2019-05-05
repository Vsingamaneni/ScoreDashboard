package com.sports.cricket.service;

import com.sports.cricket.dao.RegistrationDao;
import com.sports.cricket.model.Register;
import com.sports.cricket.model.Restrictions;
import com.sports.cricket.model.Review;
import com.sports.cricket.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service("registrationService")
public class RegistrationServiceImpl implements RegistrationService, Serializable {

    RegistrationDao registrationDao;

    @Autowired
    public void setRegistrationDao(RegistrationDao registrationDao) {
        this.registrationDao = registrationDao;
    }

    @Override
    public boolean registerUser(Register registration) {
       return registrationDao.registerUser(registration);
    }

    @Override
    public UserLogin loginUser(UserLogin userLogin) {
        return registrationDao.loginUser(userLogin);
    }

    @Override
    public Register getUser(String emailId) {
        return registrationDao.getUser(emailId);
    }

    @Override
    public boolean updateUser(Register register) {
        return registrationDao.updatePassword(register);
    }

    @Override
    public List<Register> getAllUsers() {
        return registrationDao.getAllUsers();
    }

    @Override
    public List<Restrictions> getRestrictions() {
        return registrationDao.getRestrictions();
    }

    @Override
    public boolean optOutUser(Integer memberId, String optOut) {
        return registrationDao.optOutUser(memberId, optOut);
    }

    @Override
    public boolean saveReview(Review review) {
        return registrationDao.saveReview(review);
    }

    @Override
    public Review getReview(int memberId) {
        return registrationDao.getReview(memberId);
    }

    @Override
    public List<Review> getAllReviews() {
        return registrationDao.getAllReviews();
    }


}
