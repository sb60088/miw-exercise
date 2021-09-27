package com.miw.server.repository;

import com.miw.server.controller.DatasourceConfig;
import com.miw.server.domain.ItemDvo;
import com.miw.server.repository.InventoryRepository;
import com.miw.server.service.InventoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {InventoryServiceImpl.class, InventoryRepository.class, DatasourceConfig.class})
public class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    public void test_listInventory() {
        List<ItemDvo> itemList = inventoryRepository.listInventory();
        Assertions.assertTrue(itemList.size() > 0);
        Assertions.assertTrue(itemList.stream().anyMatch(itemDvo -> itemDvo.getItemId().equals("item_1")));
    }

    @Test
    public void test_listInventoryByItemId() {
        List<ItemDvo> itemList = inventoryRepository.listInventoryById("item_1");
        Assertions.assertTrue(itemList.size() == 1);
        Assertions.assertTrue(itemList.stream().anyMatch(itemDvo -> itemDvo.getItemId().equals("item_1")));
    }

    @Test
    public void test_listInventoryByItemId_NoContent() {
        List<ItemDvo> itemList = inventoryRepository.listInventoryById("item_xyz");
        Assertions.assertTrue(itemList.size() == 0);
        Assertions.assertFalse(itemList.stream().anyMatch(itemDvo -> itemDvo.getItemId().equals("item_1")));
    }

    @Test
    public void test_getItemCount() {
        int count = inventoryRepository.getItemCount("item_1");
        Assertions.assertTrue(count == 10);
    }

    @Test
    public void test_getItemCountAs0_for_nonExisting_itemId() {
        int count = inventoryRepository.getItemCount("item_xyz");
        Assertions.assertTrue(count == 0);
    }

    @Test
    public void test_updateItemCount() {
        List<ItemDvo> items = inventoryRepository.listInventoryById("item_1");
        Assertions.assertTrue(items.size() == 1);
        Assertions.assertTrue(items.get(0).getItemCount() == 10);

        inventoryRepository.updateItemCount("item_1", 100);
        List<ItemDvo> updatedItems = inventoryRepository.listInventoryById("item_1");

        Assertions.assertTrue(updatedItems.size() == 1);
        Assertions.assertTrue(updatedItems.get(0).getItemCount() == 100);
    }

    @Test
    public void test_addVisitorsById() {
        List<ItemDvo> items = inventoryRepository.listInventoryById("item_2");
        Assertions.assertTrue(items.size() == 1);
        Assertions.assertTrue(items.get(0).getIpAddr() == null);

        inventoryRepository.addVisitorById("item_2", "192.168.1.100");
        List<ItemDvo> updatedItems = inventoryRepository.listInventoryById("item_2");

        Assertions.assertTrue(updatedItems.size() == 1);
        Assertions.assertTrue(updatedItems.stream().anyMatch(itemDvo -> itemDvo.getIpAddr().equals("192.168.1.100")));
    }
}
