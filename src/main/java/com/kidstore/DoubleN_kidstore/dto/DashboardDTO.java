package com.kidstore.DoubleN_kidstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class DashboardDTO {
    private double totalRevenue;
    private long totalOrders;
    private long totalCustomers;
    private long totalProducts;
    private List<Double> monthlyRevenue; // Cho biểu đồ
}