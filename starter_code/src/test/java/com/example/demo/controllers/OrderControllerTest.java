package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit() {

        User user =UserControllerTest.createUserPlusCart();
        when(userRepository.findByUsername("Alex")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(createOrders(user));

        ResponseEntity<UserOrder> response = orderController.submit("Alex");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();

        assertEquals(UserControllerTest.getListOfItems(), order.getItems());
        assertEquals(user.getId(), order.getUser().getId());

    }

    @Test
    public void submit_not_happy_path() {

        User user =UserControllerTest.createUserPlusCart();
        when(userRepository.findByUsername("Alex")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(createOrders(user));

        ResponseEntity<UserOrder> response = orderController.submit("Unknown");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        UserOrder order = response.getBody();
        assertNull(order);

    }


    @Test
    public void getHistoryOrdersForUser() {

        User user =UserControllerTest.createUserPlusCart();
        when(userRepository.findByUsername("Alex")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(createOrders(user));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Alex");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();

        assertEquals(createOrders(user).size(), orders.size());

    }


    @Test
    public void getHistoryOrdersForUser_not_happy_path() {

        User user =UserControllerTest.createUserPlusCart();
        when(userRepository.findByUsername("Alex")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(createOrders(user));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("unknown");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();
        assertNull(orders);

    }

    protected static List<UserOrder> createOrders(User user) {
        List<UserOrder> orders = new ArrayList<>();

        for (int id = 0; id < 3; id++) {
            UserOrder order = new UserOrder();
            Cart cart = user.getCart();
            order.setId(Long.valueOf(id));
            order.setItems(cart.getItems());
            order.setTotal(cart.getTotal());
            order.setUser(user);
            orders.add(order);
        }
        return orders;
    }
}
