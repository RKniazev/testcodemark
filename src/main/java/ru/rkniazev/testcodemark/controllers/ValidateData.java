package ru.rkniazev.testcodemark.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.rkniazev.testcodemark.models.Role;
import ru.rkniazev.testcodemark.models.RoleRepository;
import ru.rkniazev.testcodemark.models.User;
import ru.rkniazev.testcodemark.models.UserRepository;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
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

    public static boolean isValidRoles(@Autowired RoleRepository roleRepository, User user){
        List<Role> validRoles = new ArrayList();
        for (Role role : user.getRoles()) {
            if(!roleRepository.findById(role.getId()).isEmpty()){
                Role findedRole = roleRepository.findById(role.getId()).get();
                validRoles.add(findedRole);
            } else {
                return false;
            }
        }
        user.setRoles(validRoles);
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
    public static String jsonStringToLoginUser(String str){
        Status status = new Status(false);
        try {
            String login = new JSONObject(str).getString("login");
            return login;
        } catch (Exception e){
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST);
        }
    }
}
