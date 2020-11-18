package ru.rkniazev.testcodemark.controllers;

import org.springframework.web.bind.annotation.*;
import ru.rkniazev.testcodemark.models.Role;
import ru.rkniazev.testcodemark.models.RoleRepository;
import ru.rkniazev.testcodemark.models.User;
import ru.rkniazev.testcodemark.models.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    String add(@RequestParam("name")String name,
               @RequestParam("login")String login,
               @RequestParam("password")String password,
               @RequestParam(value = "roles")Long... rolesId) {
        Role role;
        User user;
        Status status = new Status(true);
        if (!ValidateData.isValidPassword(password)){
            status.setSuccess(false);
            status.addErrors("Password hasn't uppercase letter or number");
        }
        if (!ValidateData.isValidRoles(roleRepository,rolesId)){
            status.setSuccess(false);
            status.addErrors("Role/roles not found");
        }
        if (userRepository.existsById(login)){
            status.setSuccess(false);
            status.addErrors("This login already exists");
        }
        if (status.isSuccess()){
            user = new User(name, login, password);
            for (Long aLong : rolesId) {
                user.setRole(roleRepository.findById(aLong).get());
            }
            userRepository.saveAndFlush(user);
        }

        return status.toString();
    }

    @GetMapping("/users")
    String get(@RequestParam(value = "login", required=false)String login) {
        Status status = new Status(false);
        if (login == null){
            status.setSuccess(true);
            status.setBody(userRepository.findAll().toString());
        } else if (!ValidateData.isValidUserId(userRepository,login)){
            status.setSuccess(false);
            status.addErrors("Not find user by login");
        } else {
            status.setSuccess(true);
            status.setBody(userRepository.findByLogin(login).toStringWithRoles());
        }

        return status.toString();
    }

    @DeleteMapping("/users")
    String delete(@RequestParam(value = "login")String login) {
        Status status = new Status(false);
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
    String update(@RequestParam("name")String name,
                  @RequestParam("login")String login,
                  @RequestParam("password")String password,
                  @RequestParam(value = "roles")Long... roles) {
        Status status = new Status(false);
        User user;
        if (!userRepository.existsById(login)){
            status.addErrors("Not find user by login");
            return status.toString();
        } else if (!ValidateData.isValidRoles(roleRepository,roles)){
            status.setSuccess(false);
            status.addErrors("Role/roles not found");
        } else {
            user = userRepository.findByLogin(login);
            user.setName(name);
            user.setPassword(password);
            List<Role> listRole = Arrays.stream(roles)
                    .map(it -> roleRepository.findById(it).get())
                    .collect(Collectors.toList());
            user.setRoles(listRole);
            userRepository.deleteById(login);
            userRepository.saveAndFlush(user);
            status.setSuccess(true);
        }
        return status.toString();
    }
}
