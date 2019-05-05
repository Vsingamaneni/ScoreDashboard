package com.sports.cricket.dao;


import com.sports.cricket.model.Register;
import com.sports.cricket.model.Restrictions;
import com.sports.cricket.model.Review;
import com.sports.cricket.model.UserLogin;

import java.util.List;

public interface RegistrationDao {

    boolean registerUser(Register registration);

    UserLogin loginUser(UserLogin userLogin);

    Register getUser(String emailId);

    boolean updatePassword(Register register);

    List<Register> getAllUsers();

    List<Restrictions> getRestrictions();

    boolean optOutUser(Integer memberId, String optOut);

    boolean saveReview(Review review);

    Review getReview(int memberId);

    List<Review> getAllReviews();
}
