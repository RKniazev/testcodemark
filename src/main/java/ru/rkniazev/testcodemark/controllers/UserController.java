package ru.rkniazev.testcodemark.controllers;

import org.springframework.web.bind.annotation.*;
import ru.rkniazev.testcodemark.models.RoleRepository;
import ru.rkniazev.testcodemark.models.User;
import ru.rkniazev.testcodemark.models.UserRepository;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    UserController(UserRepository userRepository,
                   RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/users")
    public String add(@RequestBody User user) {
        Status status = ValidateData.validateUserForPost(user);

        if (status.isSuccess()){
            userRepository.saveAndFlush(user);
        }

        return status.toString();
    }

    @GetMapping("/users")
    String get(@RequestBody(required=false) String str) {
        Status status = new Status(true);
        String login;

        if (str == null){
            status.setBody(userRepository.findAll().toString());
            return status.toString();
        }

        status = ValidateData.validateUserId(str);

        if (status.isSuccess()){
            login = ValidateData.jsonStringToLoginUser(str);
            status.setBody(userRepository.findByLogin(login).toStringWithRoles());
        }

        return status.toString();
    }

    @DeleteMapping(value = "/users")
    public String delete(@RequestBody String str) {
        String login = ValidateData.jsonStringToLoginUser(str);
        Status status = ValidateData.validateUserId(str);

        if (status.isSuccess()){
            userRepository.delete(userRepository.findByLogin(login));
        }

        return status.toString();
    }

    @PutMapping("/users")
    public String update(@RequestBody User user) {
        Status status = ValidateData.validateUserForPut(user);

        if (status.isSuccess()){
            userRepository.saveAndFlush(user);
        }

        return status.toString();
    }
}
