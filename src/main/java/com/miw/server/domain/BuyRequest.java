package com.miw.server.domain;

import lombok.Data;

@Data
public class BuyRequest {
    private String itemId; // currently single item
    private String userId;
    private double price;
}
