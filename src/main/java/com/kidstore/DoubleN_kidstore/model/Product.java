package com.kidstore.DoubleN_kidstore.model;

import jakarta.persistence.*;
import java.text.Normalizer;
import java.util.regex.Pattern;
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price;

    private String image;

    private String color;

    private String gender;

    private String size;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // ===== GETTER =====

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getGender() {
        return gender;
    }

    public Category getCategory() {
        return category;
    }

    // ===== SETTER =====

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    public void setSize(String size) { this.size = size; }
    // ===== SEO SLUG =====
    public String getSlug() {
        if (this.name == null) return "";
        try {
            // Chuyển tiếng Việt có dấu thành không dấu
            String temp = Normalizer.normalize(this.name, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            String slug = pattern.matcher(temp).replaceAll("").toLowerCase();

            // Xóa các ký tự đặc biệt, thay khoảng trắng bằng dấu gạch ngang
            return slug.replaceAll("[^a-z0-9\\s]", "").trim().replaceAll("\\s+", "-");
        } catch (Exception ex) {
            return "san-pham"; // Trả về mặc định nếu lỗi
        }
    }
}