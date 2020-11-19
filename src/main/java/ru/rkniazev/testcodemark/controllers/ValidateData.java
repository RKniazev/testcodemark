package ru.rkniazev.testcodemark.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.rkniazev.testcodemark.models.Role;
import ru.rkniazev.testcodemark.models.RoleRepository;
import ru.rkniazev.testcodemark.models.User;
import ru.rkniazev.testcodemark.models.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidateData {
    @Autowired
    private static UserRepository userRepository;

    @Autowired
    private static RoleRepository roleRepository;

    ValidateData(UserRepository userRepository,
                 RoleRepository roleRepository) {
        ValidateData.userRepository = userRepository;
        ValidateData.roleRepository = roleRepository;
    }

    private static Status baseValidateUserForPostAndPut(User user){
        Status result = new Status(true);
        if (!isValidPassword(user.getPassword())){
            result.addErrors(Errors.PASSWORD.toString());
        }
        if (!isValidRoles(user)){
            result.addErrors(Errors.ROLES_NOT_CORRECT.toString());
        }
        if (user.getRoles().size() == 0){
            result.addErrors(Errors.ROLES_EMPTY.toString());
        }
        return result;
    }

    public static Status validateUserForPost(User user){
        Status result = baseValidateUserForPostAndPut(user);
        if (userRepository.existsById(user.getLogin())){
            result.addErrors(Errors.LOGIN_EXIST.toString());
        }
        return result;
    }

    public static Status validateUserForPut(User user){
        Status result = baseValidateUserForPostAndPut(user);
        if (!userRepository.existsById(user.getLogin())){
            result.addErrors(Errors.USER_NOT_FIND.toString());
        }
        return result;
    }

    public static Status validateUserId(String str){
        Status result = new Status(true);
        String login = ValidateData.jsonStringToLoginUser(str);
        User user = userRepository.findByLogin(login);
        if (user == null){
            result.addErrors(Errors.USER_NOT_FIND.toString());
            return result;
        }
        return result;
    }

    public static boolean isValidPassword(String pass){
        String regex = "(?=.*\\p{Lu})(?=.*\\d)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pass);
        return matcher.find();
    }

    public static boolean isValidRoles(User user){
        List<Role> validRoles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            if(roleRepository.findById(role.getId()).isPresent()){
                Role findedRole = roleRepository.findById(role.getId()).get();
                validRoles.add(findedRole);
            } else {
                return false;
            }
        }
        user.setRoles(validRoles);
        return true;
    }

    public static String jsonStringToLoginUser(String str){
        Status status = new Status(false);
        try {
            return new JSONObject(str).getString("login");
        } catch (Exception e){
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST);
        }
    }
}
