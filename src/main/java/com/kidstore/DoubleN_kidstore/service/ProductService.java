package com.kidstore.DoubleN_kidstore.service;

import com.kidstore.DoubleN_kidstore.model.Product;
import com.kidstore.DoubleN_kidstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import com.kidstore.DoubleN_kidstore.repository.OrderDetailRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getByGender(String gender) {
        return productRepository.findByGender(gender);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    // Lấy Top sản phẩm bán chạy
    public List<Product> getBestSellers(int limit) {
        try {
            // Lấy Top danh sách từ Database (từ vị trí 0, số lượng = limit)
            return orderDetailRepository.findTopSellingProducts(PageRequest.of(0, limit));
        } catch (Exception e) {
            return List.of(); // Đề phòng shop mới mở chưa có ai mua thì trả về list rỗng tránh sập web
        }
    }
}