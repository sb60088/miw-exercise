package com.miw.server.repository;

import com.miw.server.domain.ItemDvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InventoryRepository {

    private static final String LIST_INVENTORY = "select t.* from (\n" +
            "select row_number() over (partition by v.item_id order by v.visit_time desc) as rn , i.item_id, i.item_Desc,i.item_price,i.item_count,v.ip_addr, v.visit_time from items i left outer join visitors v on v.item_id=i.item_id\n" +
            ") t where t.rn < 11;";
    private static final String LIST_INVENTORY_BY_ID = "select t.* from (\n" +
            "select row_number() over (partition by v.item_id order by v.visit_time desc) as rn , i.item_id, i.item_Desc,i.item_price,i.item_count, v.ip_addr, v.visit_time from items i  left outer join visitors v on  v.item_id=i.item_id where i.item_id = '%s' \n" +
            ") t where t.rn < 11;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ItemDvo> listInventory() {
        return jdbcTemplate.query(LIST_INVENTORY, new RowMapper<ItemDvo>() {
            @Override
            public ItemDvo mapRow(ResultSet rs, int rowNum) throws SQLException {
                ItemDvo item = new ItemDvo();
                item.setItemId(rs.getString("ITEM_ID"));
                item.setItemDesc(rs.getString("ITEM_DESC"));
                item.setItemPrice(rs.getDouble("ITEM_PRICE"));
                item.setItemCount(rs.getInt("ITEM_COUNT"));
                item.setIpAddr(rs.getString("IP_ADDR"));
                item.setVisitTime(rs.getTimestamp("VISIT_TIME"));
                return item;
            }
        });
    }

    public int getItemCount(String itemId) {
        List<Integer> counts = jdbcTemplate.query(String.format("SELECT item_count from items where item_id = '%s'", itemId),
                (rs, rowNum) -> rs.getInt("ITEM_COUNT"));

        return CollectionUtils.isEmpty(counts) ? 0 : counts.get(0);
    }

    public List<ItemDvo> listInventoryById(String itemId) {
        String query = String.format(LIST_INVENTORY_BY_ID, itemId);
        return jdbcTemplate.query(query, new RowMapper<ItemDvo>() {
            @Override
            public ItemDvo mapRow(ResultSet rs, int rowNum) throws SQLException {
                ItemDvo item = new ItemDvo();
                item.setItemId(rs.getString("ITEM_ID"));
                item.setItemDesc(rs.getString("ITEM_DESC"));
                item.setItemPrice(rs.getDouble("ITEM_PRICE"));
                item.setItemCount(rs.getInt("ITEM_COUNT"));
                item.setIpAddr(rs.getString("IP_ADDR"));
                item.setVisitTime(rs.getTimestamp("VISIT_TIME"));
                return item;
            }
        });

    }

    // use merge using command to update timestamp for same item_id, ip_Addr
    public void addVisitorById(String itemId, String visitorIpAddr) {
        jdbcTemplate.update(String.format("INSERT INTO VISITORS (ITEM_ID, IP_ADDR, VISIT_TIME) VALUES ('%s','%s',current_timestamp())", itemId, visitorIpAddr));
    }

    // use merge using command to update timestamp for same item_id, ip_Addr
    public void updateItemCount(String itemId, int count) {
        jdbcTemplate.update(String.format("UPDATE ITEMS set item_count = %s where item_id = '%s'", count, itemId));
    }

}
