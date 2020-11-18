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

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TestGetUserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserController userController;


	@Test
	public void addCorrectlyUser() {

		String add = userController.add("Adam",
				"login_test1",
				"Pass1",
				1L);

		User found = userRepository.findByLogin("login_test1");

		assertThat(found.getName()).isEqualTo("Adam");
		assertThat(found.getLogin()).isEqualTo("login_test1");
		assertThat(found.getPassword()).isEqualTo("Pass1");
		assertThat(found.getRoles().get(0).toString())
				.isEqualTo(roleRepository.findById(1L).get().toString());
	}

	@Test
	public void addUserWithBadPass() {
		String result = userController.add("Adam",
				"login_test2",
				"pass1",
				1L);

		Status status = new Status(false);
		status.addErrors("Password hasn't uppercase letter or number");
		String errorBadPass = status.toString();

		assertThat(result).isEqualTo(errorBadPass);
	}

	@Test
	public void addUserWithBadRole() {
		String result = userController.add("Adam",
				"login_test3",
				"Pass1",
				12L);

		Status status = new Status(false);
		status.addErrors("Role/roles not found");
		String errorBadRole = status.toString();

		assertThat(result).isEqualTo(errorBadRole);
	}

	@Test
	public void addUserWithoutRole() {
		Long[] emptyArray = new Long[0];
		String result = userController.add("Adam",
				"login_test4",
				"Pass1",
				emptyArray);

		Status status = new Status(false);
		status.addErrors("Argument roles is empty");
		String errorEmptyArray = status.toString();

		assertThat(result).isEqualTo(errorEmptyArray);
	}

	@Test
	public void addUserWithDuplicateKay() {
		userController.add("Adam",
				"login_test5",
				"Pass1",
				1L);

		String result = userController.add("Adam",
				"login_test5",
				"Pass1",
				1L);

		Status status = new Status(false);
		status.addErrors("This login already exists");
		String errorEmptyArray = status.toString();

		assertThat(result).isEqualTo(errorEmptyArray);
	}
}
