package com.miw.server.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ItemDvo {
    private String itemId;
    private String itemDesc;
    private double itemPrice;
    private int itemCount;
    private String ipAddr;
    private Date visitTime;
}
