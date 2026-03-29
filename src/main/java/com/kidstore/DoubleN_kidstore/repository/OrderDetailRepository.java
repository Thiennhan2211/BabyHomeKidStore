package com.kidstore.DoubleN_kidstore.repository;

import com.kidstore.DoubleN_kidstore.entity.OrderDetail;
import com.kidstore.DoubleN_kidstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderId(Long orderId);
    // Lấy danh sách sản phẩm bán chạy nhất
    @Query("SELECT od.product FROM OrderDetail od GROUP BY od.product.id ORDER BY SUM(od.quantity) DESC")
    List<Product> findTopSellingProducts(Pageable pageable);

    // Truy vấn lấy top 4 sản phẩm bán chạy nhất (dựa trên tổng số lượng đã bán)
    @Query("SELECT od.product FROM OrderDetail od " +
            "WHERE od.order.status = 'APPROVED' OR od.order.status = 'COMPLETED' " +
            "GROUP BY od.product " +
            "ORDER BY SUM(od.quantity) DESC")
    List<Product> findTopBestSellers(Pageable pageable);
}