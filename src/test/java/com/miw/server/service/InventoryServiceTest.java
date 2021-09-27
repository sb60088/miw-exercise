package com.miw.server.service;

import com.miw.server.controller.DatasourceConfig;
import com.miw.server.domain.BuyRequest;
import com.miw.server.domain.Item;
import com.miw.server.domain.ItemRequest;
import com.miw.server.repository.InventoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {InventoryServiceImpl.class, InventoryRepository.class, DatasourceConfig.class})
public class InventoryServiceTest {

    @Autowired
    private InventoryServiceImpl inventoryService;

    @Test
    public void test_listInventory_surgePrice() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setItemId("item_1");
        for (int i = 0; i < 11; i++) {
            itemRequest.setIpAddr("192.168.1." + i);
            inventoryService.listItemById(itemRequest);
        }
        List<Item> items = inventoryService.listAllInventory();
        Assertions.assertTrue(items.size() > 1);
        Assertions.assertTrue(items.stream().filter(item -> item.getItemId().equals("item_1")).anyMatch(item -> item.getItemPrice() == 22));
        Assertions.assertTrue(items.stream().filter(item -> item.getItemId().equals("item_2")).anyMatch(item -> item.getItemPrice() == 50));
    }

    @Test
    public void test_listInventoryById_surgePrice() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setItemId("item_1");
        for (int i = 0; i < 11; i++) {
            itemRequest.setIpAddr("192.168.1." + i);
            inventoryService.listItemById(itemRequest);
        }
        Optional<Item> item = inventoryService.listItemById(itemRequest);
        Assertions.assertTrue(item.get().getItemPrice() == 22);
    }

    @Test
    public void test_listInventoryById_NosurgePrice() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setItemId("item_3");
        itemRequest.setIpAddr("192.168.1.1");
        inventoryService.listItemById(itemRequest);
        Optional<Item> item = inventoryService.listItemById(itemRequest);
        Assertions.assertTrue(item.get().getItemPrice() == 400.23);
    }

    @Test
    public void test_buyOrder() {
        BuyRequest buyRequest = new BuyRequest();
        buyRequest.setItemId("item_2");

        inventoryService.buyOrder(buyRequest);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setItemId("item_2");
        Optional<Item> item = inventoryService.listItemById(itemRequest);
        Assertions.assertTrue(item.get().getItemCount() == 9);
    }
}
