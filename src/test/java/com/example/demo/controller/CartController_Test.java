package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartController_Test {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void SetUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

    }

    @Test
    public void addToCart_Test() throws Exception {
        when(userRepository.findByUsername("test")).thenReturn(getUserFor_Test());
        when(itemRepository.findById(0L)).thenReturn(getItemFor_Test());
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Cart body = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, body.getItems().size());
        assertEquals(getItemFor_Test().get().getName(), body.getItems().get(0).getName());
        assertEquals(0L, body.getItems().get(0).getId());
    }

    @Test
    public void removeFromCart_Test() throws Exception {
        when(userRepository.findByUsername("test")).thenReturn(getUserFor_Test());
        when(itemRepository.findById(0L)).thenReturn(getItemFor_Test());
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("test");
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        Cart body = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, body.getItems().size());

    }

    @Test
    public void removeFromCart_UserError_Test() throws Exception {
        when(userRepository.findByUsername("test")).thenReturn(getUserFor_Test());
        when(itemRepository.findById(0L)).thenReturn(getItemFor_Test());
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("user"); // Username is different
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(404, response.getStatusCodeValue());
    }

    private User getUserFor_Test() {
        User user = new User();
        Cart cart = new Cart();
        user.setUsername("test");
        user.setPassword("test-password");
        cart.setUser(user);
        user.setCart(cart);
        return user;
    }

    private Optional<Item> getItemFor_Test() {
        Item item = new Item();
        item.setId(0L);
        item.setName("Laptop");
        item.setPrice(new BigDecimal(999));
        item.setDescription("Microsoft i7 v5");
        return Optional.of(item);
    }

}
