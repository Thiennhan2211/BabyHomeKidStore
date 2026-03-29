package com.kidstore.DoubleN_kidstore.repository;

import com.kidstore.DoubleN_kidstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 1. Tìm theo giới tính (boy / girl)
    List<Product> findByGender(String gender);

    // 2. Tìm theo loại đồ (shirt, pants, set...)
    List<Product> findByCategory(String category);

    // 3. Tìm kiếm theo từ khóa (cho thanh search)
    List<Product> findByNameContainingIgnoreCase(String keyword);
}