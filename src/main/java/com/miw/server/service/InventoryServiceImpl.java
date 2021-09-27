package com.miw.server.service;

import com.miw.server.domain.BuyRequest;
import com.miw.server.domain.Item;
import com.miw.server.domain.ItemDvo;
import com.miw.server.domain.ItemRequest;
import com.miw.server.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<Item> listAllInventory() {
        return surgeItemsAny(inventoryRepository.listInventory());
    }

    @Override
    public Optional<Item> listItemById(ItemRequest itemRequest) {
        List<ItemDvo> items = inventoryRepository.listInventoryById(itemRequest.getItemId());
        if (CollectionUtils.isEmpty(items)) {
            return Optional.empty();
        }
        inventoryRepository.addVisitorById(itemRequest.getItemId(), itemRequest.getIpAddr());
        return Optional.of(surgeItemsAny(items).get(0));
    }

    @Override
    public void buyOrder(BuyRequest buyRequest) {
        int itemCount = inventoryRepository.getItemCount(buyRequest.getItemId());
        if (itemCount == 0) {
            throw new RuntimeException("Cannot process buy order as no stock is left");
        }
        inventoryRepository.updateItemCount(buyRequest.getItemId(), --itemCount);
    }

    private List<Item> surgeItemsAny(List<ItemDvo> itemDvos) {
        if (CollectionUtils.isEmpty(itemDvos)) {
            return new ArrayList<>();
        }

        Map<String, List<ItemDvo>> items = itemDvos.stream().collect(Collectors.groupingBy(ItemDvo::getItemId));
        return items.entrySet().stream().map(stringListEntry -> {
            Item item = new Item();
            item.setItemId(stringListEntry.getKey());

            List<ItemDvo> dvos = stringListEntry.getValue();
            double price = 0.0;
            ItemDvo itemDvo = dvos.get(0);
            if (dvos.size() >= 10 && visitIn1Hour(itemDvo)) {
                price = itemDvo.getItemPrice() * 1.1;
            } else {
                price = itemDvo.getItemPrice();
            }
            item.setItemDesc(itemDvo.getItemDesc());
            item.setItemPrice(price);
            item.setItemCount(itemDvo.getItemCount());
            return item;
        }).collect(Collectors.toList());
    }

    private boolean visitIn1Hour(ItemDvo itemDvo) {
        long minutes = (new Date().getTime() - itemDvo.getVisitTime().getTime()) / (1000 * 60);
        return minutes < 60;
    }
}
