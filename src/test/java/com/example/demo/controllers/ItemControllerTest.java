package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class ItemControllerTest {

    private final static Logger logger = LoggerFactory.getLogger(ItemControllerTest.class);

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private Item testItem;

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
        testItem = new Item();
        testItem.setId(0L);
        testItem.setName("UniqueItem");
        testItem.setPrice(new BigDecimal("8.99"));
        testItem.setDescription("1_AnItemOnlyForTestPurposes");
        itemRepository.save(testItem);
    }


    @Test
    public void getItems_happy_path(){
        logger.debug("Testing: getItemsInTheInventory...");
        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

    }


    @Test
    public void getItemByIdNotFound(){
        logger.debug("Testing: getItemByIdNotFound...");
        final ResponseEntity<Item> response = itemController.getItemById(100L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemById_happy_path(){
        logger.debug("Testing: getItemById_happy_path...");
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.ofNullable(testItem));
        final ResponseEntity<Item> response = itemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getItemByNameNotFound(){
        logger.debug("Testing: getItemByNameNotFound...");
        when(itemRepository.findByName("Item")).thenReturn(null);
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Item");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemByName_happy_path()
    {
        logger.debug("Testing: getItemByName_happy_path...");
        when(itemRepository.findByName("UniqueItem")).thenReturn(Collections.singletonList(testItem));
        final ResponseEntity<List<Item>> response = itemController.getItemsByName(testItem.getName());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

}
