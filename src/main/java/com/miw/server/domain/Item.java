package com.miw.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Item {
    private String itemId;
    private String itemDesc;
    private double itemPrice;
    private int itemCount;
    @JsonIgnore
    private List<VisitInfo> visitInfoList = new ArrayList<>();

    @Data
    public static class VisitInfo {
        private String ipAddr;
        private Date visitTime;
    }
}
