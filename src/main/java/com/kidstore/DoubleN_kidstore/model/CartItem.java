package com.kidstore.DoubleN_kidstore.model;

public class CartItem {
    private Long productId;
    private String name;
    private double price;
    private String image;
    private String color;
    private String size;
    private int quantity;

    // Constructor
    public CartItem(Long productId, String name, double price, String image, String color, String size, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.color = color;
        this.size = size;
        this.quantity = quantity;
    }

    // Tự generate Getter và Setter cho tất cả các trường ở đây nhé
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}