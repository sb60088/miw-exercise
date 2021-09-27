package com.miw.server.controller;

import com.miw.server.domain.Item;
import com.miw.server.domain.ItemRequest;
import com.miw.server.service.InventoryService;
import com.miw.server.service.InventoryServiceImpl;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventory/")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping(value = "items/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllInventory() {
        try {
            List<Item> items = inventoryService.listAllInventory();
            if (CollectionUtils.isEmpty(items)) {
                return new ResponseEntity("No products found in the inventory", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity(ExceptionUtils.getStackTrace(exception), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "item", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getItemById(@RequestBody ItemRequest itemRequest) {
        try {
            Optional<Item> item = inventoryService.listItemById(itemRequest);
            if (item.isPresent()) {
                return new ResponseEntity<>(item.get(), HttpStatus.OK);
            }
            return new ResponseEntity("No Item found in the inventory", HttpStatus.NO_CONTENT);
        } catch (Exception exception) {
            return new ResponseEntity(ExceptionUtils.getStackTrace(exception), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
