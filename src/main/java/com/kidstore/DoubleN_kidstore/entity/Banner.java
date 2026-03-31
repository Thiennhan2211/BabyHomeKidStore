package com.kidstore.DoubleN_kidstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "banners")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image; // Cột này sẽ chứa link Cloudinary

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}