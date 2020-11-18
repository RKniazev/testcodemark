package ru.rkniazev.testcodemark;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.rkniazev.testcodemark.controllers.Status;
import ru.rkniazev.testcodemark.controllers.UserController;
import ru.rkniazev.testcodemark.models.Role;
import ru.rkniazev.testcodemark.models.RoleRepository;
import ru.rkniazev.testcodemark.models.User;
import ru.rkniazev.testcodemark.models.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestPutUserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserController userController;

    @Test
    public void updateCorrectlyUser() {
        User user = new User("Adam", "login_test1", "Pass1");
        user.setRole(roleRepository.findById(1L).get());
        userController.add(user);

        user.setName("Ivan");
        user.setRole(roleRepository.findById(2L).get());
        user.setPassword("Bestpass111");

        userController.update(user);

        User found = userRepository.findByLogin("login_test1");

        assertThat(found.toStringWithRoles()).isEqualTo(user.toStringWithRoles());
    }

    @Test
    public void updateWithBadPass() {
        User user = new User("Adam", "login_test2", "Pass1");
        user.setRole(roleRepository.findById(1L).get());
        userController.add(user);

        user.setPassword("Pass-without-num");
        String result1 = userController.update(user);

        user.setPassword("123 pass-without-upper");
        String result2 = userController.update(user);

        Status status = new Status(false);
        status.addErrors("Password hasn't uppercase letter or number");
        String errorBadPass = status.toString();

        assertThat(result1).isEqualTo(errorBadPass);
        assertThat(result2).isEqualTo(errorBadPass);
    }

    @Test
    public void updateUserWithoutRole() {
        User user = new User("Adam", "login_test4", "Pass1");
        user.setRole(roleRepository.findById(1L).get());
        userController.add(user);

        List<Role> emptyList = new ArrayList<>();
        user.setRoles(emptyList);

        String result = userController.update(user);

        Status status = new Status(false);
        status.addErrors("Argument roles is empty");
        String errorEmptyArray = status.toString();

        assertThat(result).isEqualTo(errorEmptyArray);
    }

    @Test
    public void updateBadLogin() {
        User user = new User("Adam", "login_test2", "Pass1");
        user.setRole(roleRepository.findById(1L).get());
        userController.add(user);

        User user2 = new User("Adam", "login_test22", "Pass1");
        user2.setRole(roleRepository.findById(1L).get());

        String result = userController.update(user2);

        Status status = new Status(false);
        status.addErrors("Not find user by login");

        assertThat(result).isEqualTo(status.toString());
    }
}
