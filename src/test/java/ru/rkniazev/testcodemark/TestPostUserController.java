package ru.rkniazev.testcodemark;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
class TestPostUserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserController userController;


	@Test
	public void addCorrectlyUser() {
	    User user = new User("Adam", "login_test1", "Pass1");
	    user.setRole(roleRepository.findById(1L).get());
		String add = userController.add(user);

		User found = userRepository.findByLogin("login_test1");

		assertThat(found.toStringWithRoles()).isEqualTo(user.toStringWithRoles());
	}

	@Test
	public void addUserWithBadPass() {
		User user = new User("Adam", "login_test2", "pass1");
		user.setRole(roleRepository.findById(1L).get());
		String result = userController.add(user);

		Status status = new Status(false);
		status.addErrors("Password hasn't uppercase letter or number");
		String errorBadPass = status.toString();

		assertThat(result).isEqualTo(errorBadPass);
	}

	@Test
	public void addUserWithoutRole() {
		User user = new User("Adam", "login_test4", "Pass1");
		List<Role> emptyList = new ArrayList<>();
		user.setRoles(emptyList);
		String result = userController.add(user);

		Status status = new Status(false);
		status.addErrors("Argument roles is empty");
		String errorEmptyArray = status.toString();

		assertThat(result).isEqualTo(errorEmptyArray);
	}

	@Test
	public void addUserWithDuplicateKay() {
		User user1 = new User("Ivan", "login_test5", "Pass1");
		user1.setRole(roleRepository.findById(1L).get());
		User user2 = new User("Nik", "login_test5", "Pass2");
		user2.setRole(roleRepository.findById(1L).get());

		userController.add(user1);

		String result = userController.add(user2);

		Status status = new Status(false);
		status.addErrors("This login already exists");
		String errorEmptyArray = status.toString();

		assertThat(result).isEqualTo(errorEmptyArray);
	}
}
