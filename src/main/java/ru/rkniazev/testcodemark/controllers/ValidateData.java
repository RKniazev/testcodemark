package ru.rkniazev.testcodemark.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import ru.rkniazev.testcodemark.models.RoleRepository;
import ru.rkniazev.testcodemark.models.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateData {
    public static boolean isValidPassword(String pass){
        String regex = "(?=.*\\p{Lu})(?=.*\\d)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pass);
        return matcher.find();
    }

    public static boolean isValidRoles(@Autowired RoleRepository roleRepository,Long ... roles){
        for (Long role : roles) {
           if(roleRepository.findById(role).isEmpty()){
               return false;
           }
        }
        return true;
    }
    public static boolean isValidUserId(@Autowired UserRepository userRepository, String ... users){
        for (String userId : users) {
            if(userRepository.findById(userId).isEmpty()){
                return false;
            }
        }
        return true;
    }
}
