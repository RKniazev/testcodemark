package ru.rkniazev.testcodemark.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rkniazev.testcodemark.models.RoleRepository;
import ru.rkniazev.testcodemark.models.UserRepository;

@RestController
public class RoleController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    RoleController(UserRepository userRepository,
                   RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/roles")
    String get() {
        Status status = new Status(false);
        status.setBody(roleRepository.findAll().toString());
        status.setSuccess(true);
        return status.toString();
    }
}
