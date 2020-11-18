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
public class TestPutUserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserController userController;

    @Test
    public void updateCorrectlyUser() {

        userController.add("Adam",
                "login_test1",
                "Pass1",
                1L);

        userController.update("Ivan",
                "login_test1",
                "Bestpass111",
                2L);

        User found = userRepository.findByLogin("login_test1");

        assertThat(found.getName()).isEqualTo("Ivan");
        assertThat(found.getLogin()).isEqualTo("login_test1");
        assertThat(found.getPassword()).isEqualTo("Bestpass111");
        assertThat(found.getRoles().get(0).toString())
                .isEqualTo(roleRepository.findById(2L).get().toString());
    }

    @Test
    public void updateBadLogin() {
        userController.add("Adam",
                "login_test1",
                "Pass1",
                1L);

        String result = userController.update("Ivan",
                "das",
                "Bestpass111",
                2L);

        Status status = new Status(false);
        status.addErrors("Not find user by login");

        assertThat(result).isEqualTo(status.toString());
    }

    @Test
    public void updateEmptyRoles() {
        userController.add("Adam",
                "login_test1",
                "Pass1",
                1L);

        Long[] emptyArray = new Long[0];

        String result = userController.update("Adam",
                "login_test1",
                "Pass1",
                emptyArray);

        Status status = new Status(false);
        status.addErrors("Argument roles is empty");

        assertThat(result).isEqualTo(status.toString());
    }

    @Test
    public void updateBadRoles() {
        userController.add("Adam",
                "login_test1",
                "Pass1",
                1L);

        String resultOneRole = userController.update("Adam",
                "login_test1",
                "Pass1",
                13L);
        String resultArrayRoles = userController.update("Adam",
                "login_test1",
                "Pass1",
                13L,1L);

        Status status = new Status(false);
        status.addErrors("Role/roles not found");

        assertThat(resultOneRole).isEqualTo(status.toString());
        assertThat(resultArrayRoles).isEqualTo(status.toString());
    }
}
