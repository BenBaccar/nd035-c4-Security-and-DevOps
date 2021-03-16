package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){

        itemController= new ItemController();

        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

    }

    @Test
    public void getAllItems(){

        List <Item> listItems = Arrays.asList(createItem(1L),createItem(2L));

        when(itemRepository.findAll()).thenReturn(listItems);
        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();

        assertEquals(listItems, items);

    }

    @Test
    public void getItemById(){
        Item item=createItem(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(item.getId());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item responseItem = response.getBody();
        assertEquals(item, responseItem);


    }


    @Test
    public void getItemByName(){

        List <Item> listItems = Arrays.asList(createItem(1L),createItem(2L));

        when(itemRepository.findByName("testItem")).thenReturn(listItems);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> responseItems = response.getBody();

        assertEquals(listItems, responseItems);

    }

    protected static Item createItem(long id){

        Item item = new Item();
        item.setId(id);
        item.setPrice(BigDecimal.valueOf(2));
        item.setName("TestItem");
        item.setDescription("TestDescription");
        return item;
    }
}
