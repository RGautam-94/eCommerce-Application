package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderController_Test {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit_Test() throws Exception {
        when(userRepository.findByUsername("test")).thenReturn(getUserFor_Test());
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertEquals(200, response.getStatusCodeValue());

        UserOrder responseBody = response.getBody();
        assertEquals(getUserFor_Test().getCart().getTotal(), responseBody.getTotal());
        assertEquals(1, responseBody.getItems().size());
    }

    @Test
    public void getOrderForUser_Test() throws Exception {
        when(userRepository.findByUsername("test")).thenReturn(getUserFor_Test());
        List<UserOrder> userOrders = Arrays.asList(UserOrder.createFromCart(getUserFor_Test().getCart()));
        when(orderRepository.findByUser(any())).thenReturn(userOrders);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> responseBody = response.getBody();
        assertEquals(new BigDecimal(999), responseBody.get(0).getTotal());
        assertEquals("test", responseBody.get(0).getUser().getUsername());
        assertEquals("Laptop", responseBody.get(0).getItems().get(0).getName());
        assertEquals(0L, responseBody.get(0).getUser().getId());

    }

    @Test
    public void submit_UserError_Test() throws Exception {
        when(userRepository.findByUsername("test")).thenReturn(getUserFor_Test());
        ResponseEntity<UserOrder> response = orderController.submit("user");
        assertEquals(404, response.getStatusCodeValue());
    }

    private User getUserFor_Test() throws Exception {
        User user = new User();
        Cart cart = new Cart();
        Item item = new Item();
        user.setUsername("test");
        user.setPassword("test-password");
        cart.setUser(user);
        item.setPrice(new BigDecimal(999));
        item.setName("Laptop");
        item.setDescription("Microsoft i7 v5");
        cart.setItems(Arrays.asList(item));
        cart.setTotal(new BigDecimal(999));
        user.setCart(cart);
        return user;
    }


}