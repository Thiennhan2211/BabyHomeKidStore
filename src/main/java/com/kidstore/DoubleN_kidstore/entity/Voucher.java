package com.kidstore.DoubleN_kidstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;        // Mã (VD: BABYHOME)
    private double discount;    // Số tiền giảm (VD: 50000. Nếu freeship thì để 0)
    private double minAmount;   // Đơn tối thiểu (VD: 500000)
    private String title;       // Tiêu đề (VD: Giảm 50.000đ)
    private String description; // Mô tả (VD: Đơn tối thiểu 500k. HSD: 30/12...)
    private String color;       // Màu thẻ (blue hoặc orange)

    // Tự động Generate Getter/Setter hoặc Copy đoạn dưới đây:
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    public double getMinAmount() { return minAmount; }
    public void setMinAmount(double minAmount) { this.minAmount = minAmount; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}