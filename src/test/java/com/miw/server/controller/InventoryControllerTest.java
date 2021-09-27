package com.miw.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miw.server.domain.Item;
import com.miw.server.domain.ItemRequest;
import com.miw.server.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = InventoryController.class)
public class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InventoryService inventoryService;

    @Test
    public void when_listInventoryCalled_returnNoContent() throws Exception {
        Mockito.when(inventoryService.listAllInventory()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                .get("/inventory/items/all")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void when_listInventoryCalled_returnOK() throws Exception {
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        items.add(item);
        Mockito.when(inventoryService.listAllInventory()).thenReturn(items);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/inventory/items/all")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void when_listInventoryByIdCalled_returnOK() throws Exception {
        Item item = new Item();
        ItemRequest itemRequest = new ItemRequest();
        String stringItemRequest = new ObjectMapper().writeValueAsString(itemRequest);
        Mockito.when(inventoryService.listItemById(Mockito.any(ItemRequest.class))).thenReturn(Optional.of(item));
        mockMvc.perform(MockMvcRequestBuilders
                .post("/inventory/item")
                .content(stringItemRequest)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void when_listInventoryByIdCalled_returnNoContent() throws Exception {
        ItemRequest itemRequest = new ItemRequest();
        String stringItemRequest = new ObjectMapper().writeValueAsString(itemRequest);
        Mockito.when(inventoryService.listItemById(Mockito.any(ItemRequest.class))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/inventory/item")
                .content(stringItemRequest)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
