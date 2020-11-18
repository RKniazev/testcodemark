package ru.rkniazev.testcodemark;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rkniazev.testcodemark.controllers.Status;
import ru.rkniazev.testcodemark.controllers.UserController;
import ru.rkniazev.testcodemark.models.RoleRepository;
import ru.rkniazev.testcodemark.models.User;
import ru.rkniazev.testcodemark.models.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestDeleteUserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserController userController;

    @Test
    public void deleteCorrectlyUser() {
        User user = new User("Adam", "login_test1", "Pass1");
        user.setRole(roleRepository.findById(1L).get());
        userController.add(user);
        String json = "{\"login\" : \"login_test1\"\n}";
        long Count1 = userRepository.count();

        String result = userController.delete(json);

        long Count2 = userRepository.count();
        String status = new Status(true).toString();
        assertThat(result).isEqualTo(status);
        assertThat(Count1).isEqualTo(Count2+1);
    }

    @Test
    public void deleteBadLogin() {
        User user = new User("Adam", "login_test1", "Pass1");
        user.setRole(roleRepository.findById(1L).get());
        userController.add(user);
        String json = "{\"login\" : \"login_t1est1\"\n}";
        long Count1 = userRepository.count();
        String result = userController.delete(json);
        long Count2 = userRepository.count();

        Status status = new Status(false);
        status.addErrors("Not find user by login");

        assertThat(result).isEqualTo(status.toString());
        assertThat(Count1).isEqualTo(Count2);
    }

}
