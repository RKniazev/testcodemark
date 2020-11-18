package ru.rkniazev.testcodemark.controllers;

import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

        Status status = new Status(true);

        if (!ValidateData.isValidPassword(user.getPassword())){
            status.setSuccess(false);
            status.addErrors("Password hasn't uppercase letter or number");
        }
        if (!ValidateData.isValidRoles(roleRepository,user)){
            status.setSuccess(false);
            status.addErrors("Role/roles not found");
        }
        if (user.getRoles().size() == 0){
            status.setSuccess(false);
            status.addErrors("Argument roles is empty");
        }
        if (userRepository.existsById(user.getLogin())){
            status.setSuccess(false);
            status.addErrors("This login already exists");
        }
        if (status.isSuccess()){
            userRepository.saveAndFlush(user);
        }

        return status.toString();
    }

    @GetMapping("/users")
    String get(@RequestBody(required=false) String str) {
        Status status = new Status(false);
        if (str == null){
            status.setSuccess(true);
            status.setBody(userRepository.findAll().toString());
            return status.toString();
        }
        String login =ValidateData.jsonStringToLoginUser(str);
        if (ValidateData.isValidUserId(userRepository,login) && login != null){

            status.setSuccess(true);
            status.setBody(userRepository.findByLogin(login).toStringWithRoles());
        } else {
            status.setSuccess(false);
            status.addErrors("Not find user by login");
        }

        return status.toString();
    }

    @DeleteMapping(value = "/users")
    public String delete(@RequestBody String str) {
        Status status = new Status(false);
        String login = ValidateData.jsonStringToLoginUser(str);
        User user = userRepository.findByLogin(login);
        if (user == null){
            status.addErrors("Not find user by login");
            return status.toString();
        } else {
            userRepository.delete(userRepository.findByLogin(login));
            status.setSuccess(true);
        }
        return status.toString();
    }

    @PutMapping("/users")
    public String update(@RequestBody User user) {
        Status status = new Status(true);
        if (!ValidateData.isValidPassword(user.getPassword())){
            status.setSuccess(false);
            status.addErrors("Password hasn't uppercase letter or number");
        }
        if (!userRepository.existsById(user.getLogin())){
            status.setSuccess(false);
            status.addErrors("Not find user by login");
        }
        if (user.getRoles().size() == 0){
            status.setSuccess(false);
            status.addErrors("Argument roles is empty");
        }
        if (!ValidateData.isValidRoles(roleRepository,user)){
            status.setSuccess(false);
            status.addErrors("Role/roles not found");
        }
        if (status.isSuccess()){
            userRepository.deleteById(user.getLogin());
            userRepository.saveAndFlush(user);
            status.setSuccess(true);
        }
        return status.toString();
    }
}
