package com.kidstore.DoubleN_kidstore.repository;

import com.kidstore.DoubleN_kidstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByGender(String gender);

    // ✅ FIX: dùng category_id thay vì String
    List<Product> findByGenderAndCategory_Id(String gender, Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String keyword);

}