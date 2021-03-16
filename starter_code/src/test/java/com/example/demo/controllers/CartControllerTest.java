package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class CartControllerTest {

private static final Logger LOGGER = LoggerFactory.getLogger(CartControllerTest.class);

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock (CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){

        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

    }

    @Test
    public void addToCart(){

        when(userRepository.findByUsername("testAlex")).thenReturn(UserControllerTest.createUserPlusCart());

        when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemControllerTest.createItem(1L)));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(3);
        request.setItemId(1L);
        request.setUsername("testAlex");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart responseCart = response.getBody();
        assertNotNull(responseCart);

        assertEquals("testAlex", responseCart.getUser().getUsername());

    }

    @Test
    public void removeFromCart(){

        when(userRepository.findByUsername("testAlex")).thenReturn(UserControllerTest.createUserPlusCart());

        when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemControllerTest.createItem(1L)));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1L);
        request.setUsername("testAlex");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart responseCart = response.getBody();
        assertNotNull(responseCart);

        assertEquals("testAlex", responseCart.getUser().getUsername());

    }

    @Test
    public void addFromCart_with_invalid_username(){

        when(userRepository.findByUsername("testAlex")).thenReturn(UserControllerTest.createUserPlusCart());

        when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemControllerTest.createItem(1L)));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1L);
        request.setUsername("unknownUser");


        ResponseEntity<Cart> addResponse = cartController.addTocart(request);
        assertNotNull(addResponse);
        assertEquals(404, addResponse.getStatusCodeValue());
        Cart responseCart=addResponse.getBody();
        assertNull(responseCart);


    }

    @Test
    public void addFromCart_with_invalid_id(){

        when(userRepository.findByUsername("testAlex")).thenReturn(UserControllerTest.createUserPlusCart());

        when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemControllerTest.createItem(1L)));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1000000L);
        request.setUsername("testAlex");

        ResponseEntity<Cart> addResponse = cartController.addTocart(request);
        assertNotNull(addResponse);
        assertEquals(404, addResponse.getStatusCodeValue());
        Cart responseCart=addResponse.getBody();
        assertNull(responseCart);


    }


    @Test
    public void removeFromCart_with_invalid_username(){

        when(userRepository.findByUsername("testAlex")).thenReturn(UserControllerTest.createUserPlusCart());

        when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemControllerTest.createItem(1L)));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1L);
        request.setUsername("unknownUser");

        ResponseEntity<Cart> removeResponse = cartController.removeFromcart(request);
        assertNotNull(removeResponse);
        assertEquals(404, removeResponse.getStatusCodeValue());
        assertNull(removeResponse.getBody());


    }
    @Test
    public void removeFromCart_with_invalid_id(){

        when(userRepository.findByUsername("testAlex")).thenReturn(UserControllerTest.createUserPlusCart());

        when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemControllerTest.createItem(1L)));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1000000L);
        request.setUsername("testAlex");

        ResponseEntity<Cart> removeResponse = cartController.removeFromcart(request);
        assertNotNull(removeResponse);
        assertEquals(404, removeResponse.getStatusCodeValue());
        assertNull(removeResponse.getBody());


    }


}
