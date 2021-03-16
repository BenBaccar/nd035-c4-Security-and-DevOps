package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.naming.NameNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path(){
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testUser");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);

        assertEquals(0, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());

    }

    @Test
    public void create_user_with_invalid_password_policy(){
        when(bCryptPasswordEncoder.encode("pass")).thenReturn("hashedPassword");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testUser");
        request.setPassword("pass");
        request.setConfirmPassword("pass");

        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

        User user = response.getBody();

        assertNull(user);

    }


    @Test
    public void create_user_with_password_mismatch(){
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testUser");
        request.setPassword("testPassword");
        request.setConfirmPassword("pass");

        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

        User user = response.getBody();

        assertNull(user);

    }


    @Test
    public void findUserById(){

        User user = createUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(user.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User responseUser = response.getBody();

        assertNotNull(responseUser);

        assertEquals(user.getId(), responseUser.getId());
        assertEquals(user.getUsername(), responseUser.getUsername());
        assertEquals(user.getPassword(), responseUser.getPassword());
    }

    @Test
    public void findByUserName() throws NameNotFoundException {

        User user = createUser();

        when(userRepository.findByUsername("testAlex")).thenReturn(Optional.of(user).orElseThrow(NameNotFoundException::new));

        ResponseEntity<User> response = userController.findByUserName(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User responseUser = response.getBody();

        assertNotNull(responseUser);

        assertEquals(user.getId(), responseUser.getId());
        assertEquals(user.getUsername(), responseUser.getUsername());
        assertEquals(user.getPassword(), responseUser.getPassword());
    }

    protected static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testAlex");
        user.setPassword("testAlexPassword");
        return user;
    }

    protected static User createUserPlusCart() {
        User user = createUser();

        Cart cart = new Cart();
        cart.setId(1L);
        List<Item> itemsList= getListOfItems();
        cart.setItems((itemsList));
        cart.setTotal(itemsList.stream().map(item -> item.getPrice()).reduce(BigDecimal::add).get());
        cart.setUser(user);
        user.setCart(cart);

        return user;
    }

    protected  static List<Item> getListOfItems(){
        List<Item> itemsList= new ArrayList<>();

        itemsList.add(ItemControllerTest.createItem(1L));
        itemsList.add(ItemControllerTest.createItem(2L));
        itemsList.add(ItemControllerTest.createItem(3L));

        return itemsList;
    }

}
