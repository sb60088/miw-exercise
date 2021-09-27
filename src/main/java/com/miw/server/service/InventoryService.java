package com.miw.server.service;

import com.miw.server.domain.BuyRequest;
import com.miw.server.domain.Item;
import com.miw.server.domain.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface InventoryService {

    List<Item> listAllInventory();
    Optional<Item> listItemById(ItemRequest itemRequest);
    void buyOrder(BuyRequest buyRequest);
}
