package com.kidstore.DoubleN_kidstore.repository;

import com.kidstore.DoubleN_kidstore.entity.Order;
import com.kidstore.DoubleN_kidstore.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.kidstore.DoubleN_kidstore.entity.User;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerOrderByIdDesc(User customer);

    @Query(value = "SELECT * FROM `orders` WHERE user_id = :userId", nativeQuery = true)
    List<Order> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT o FROM Order o WHERE o.createDate BETWEEN :start AND :end " +
            "AND (o.status = 'APPROVED' OR o.status = 'COMPLETED')")
    List<Order> findSuccessfulOrdersBetween(@Param("start") Date start, @Param("end") Date end);

    // Tìm đơn hàng chờ duyệt (Pending)
    List<Order> findByStatusIgnoreCaseOrderByCreateDateDesc(String status);
}