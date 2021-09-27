package com.miw.server.controller;

import com.miw.server.domain.BuyRequest;
import com.miw.server.service.InventoryService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping(value = "buy/item", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity buyItem(@RequestBody BuyRequest buyRequest) {
        try {

            inventoryService.buyOrder(buyRequest);
            return ResponseEntity.ok("Buy Request processed successfully");
        } catch (Exception e) {
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
