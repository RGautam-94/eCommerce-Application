package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemController_Test {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItems_Test() throws Exception {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(getItem_Test()));
        ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> itemList = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(getItem_Test().getName(), itemList.get(0).getName());
        assertEquals(getItem_Test().getPrice(), itemList.get(0).getPrice());
        assertEquals(getItem_Test().getDescription(), itemList.get(0).getDescription());
    }

    @Test
    public void getItemByName_Test() throws Exception {
        when(itemRepository.findByName("Laptop")).thenReturn(Arrays.asList(getItem_Test()));
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Laptop");
        List<Item> body = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(getItem_Test().getName(), body.get(0).getName());
        assertEquals(getItem_Test().getDescription(), body.get(0).getDescription());
        assertEquals(getItem_Test().getPrice(), body.get(0).getPrice());
    }

    @Test
    public void getItemById_Test() throws Exception {
        when(itemRepository.findById(0L)).thenReturn(Optional.of(getItem_Test()));
        ResponseEntity<Item> response = itemController.getItemById(0L);
        Item body = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(getItem_Test().getName(), body.getName());
        assertEquals(getItem_Test().getDescription(), body.getDescription());
        assertEquals(getItem_Test().getPrice(), body.getPrice());
    }

    @Test
    public void getItemById_Error_Test() {
        when(itemRepository.findById(0L)).thenReturn(Optional.of(getItem_Test()));
        ResponseEntity<Item> response = itemController.getItemById(1L); // bad id
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemByName_Error_Test() {
        when(itemRepository.findByName("Laptop")).thenReturn(Arrays.asList(getItem_Test()));
        ResponseEntity<List<Item>> response = itemController.getItemsByName("NoteBook");
        assertEquals(404, response.getStatusCodeValue());
    }


    private Item getItem_Test() {
        Item item = new Item();
        item.setDescription("Core i7, v5");
        item.setPrice(new BigDecimal(999.99));
        item.setName("Laptop");
        return item;
    }

}
