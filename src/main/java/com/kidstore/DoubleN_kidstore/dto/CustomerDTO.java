package com.kidstore.DoubleN_kidstore.dto;

import com.kidstore.DoubleN_kidstore.entity.Order;
import com.kidstore.DoubleN_kidstore.entity.User;
import java.util.List;

public class CustomerDTO {
    private User user;
    private double totalSpent;
    private long orderCount;
    private String rank;
    private List<Order> orders;

    public CustomerDTO(User user, double totalSpent, long orderCount, List<Order> orders) {
        this.user = user;
        this.totalSpent = totalSpent;
        this.orderCount = orderCount;
        this.orders = orders;

        // Logic tự động phân hạng dựa trên số tiền chi tiêu
        if (totalSpent >= 5000000) {
            this.rank = "VIP";
        } else if (totalSpent >= 1000000) {
            this.rank = "LOYAL"; // Khách hàng thân thiết
        } else {
            this.rank = "NEW";   // Khách hàng mới
        }
    }

    // Các Getter (Quan trọng để Thymeleaf đọc được dữ liệu)
    public User getUser() { return user; }
    public double getTotalSpent() { return totalSpent; }
    public long getOrderCount() { return orderCount; }
    public String getRank() { return rank; }
    public List<Order> getOrders() { return orders; }
}