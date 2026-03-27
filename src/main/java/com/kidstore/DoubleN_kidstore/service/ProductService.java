package com.kidstore.DoubleN_kidstore.service;

import com.kidstore.DoubleN_kidstore.model.Product;
import com.kidstore.DoubleN_kidstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // lấy tất cả
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // lấy theo giới tính
    public List<Product> getByGender(String gender) {
        return productRepository.findByGender(gender);
    }

    // FIX: categoryId (Long)
    public List<Product> getByGenderAndCategory(String gender, Long categoryId) {
        return productRepository.findByGenderAndCategory_Id(gender, categoryId);
    }

    // THÊM HÀM NÀY ĐỂ FIX LỖI "findById"
    public Product findById(Long id) {
        // Hàm findById của JpaRepository trả về kiểu Optional,
        // dùng orElse(null) để trả về null nếu không tìm thấy ID đó trong Database
        return productRepository.findById(id).orElse(null);
    }
    // THÊM HÀM NÀY ĐỂ SERIVCE GỌI XUỐNG REPOSITORY
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}