package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.dto.CustomerDTO;
import com.kidstore.DoubleN_kidstore.entity.Order;
import com.kidstore.DoubleN_kidstore.entity.User;
import com.kidstore.DoubleN_kidstore.repository.OrderRepository;
import com.kidstore.DoubleN_kidstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminCustomerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/admin/customer")
    public String customerManagement(Model model) {
        List<User> allUsers = userRepository.findAll();

        List<CustomerDTO> dtos = allUsers.stream()
                // FIX: Sửa thành ROLE_CUSTOMER để khớp với Database của bạn
                .filter(u -> u.getRole() != null && u.getRole().equalsIgnoreCase("ROLE_CUSTOMER"))
                .map(user -> {
                    List<Order> userOrders = orderRepository.findByUserId(user.getId());
                    double totalSpent = userOrders.stream()
                            .filter(o -> "APPROVED".equalsIgnoreCase(o.getStatus()) || "COMPLETED".equalsIgnoreCase(o.getStatus()))
                            .mapToDouble(Order::getTotalAmount)
                            .sum();
                    return new CustomerDTO(user, totalSpent, userOrders.size(), userOrders);
                }).collect(Collectors.toList());

        model.addAttribute("customers", dtos);
        return "admin/customer";
    }

    // Thêm API này để lấy lịch sử đơn hàng cho Popup
    @GetMapping("/admin/customer/orders/{userId}")
    @ResponseBody
    public List<Order> getCustomerOrders(@PathVariable Integer userId) {
        return orderRepository.findByUserId(userId);
    }
}