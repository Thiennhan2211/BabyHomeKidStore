package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.entity.Order;
import com.kidstore.DoubleN_kidstore.model.Product;
import com.kidstore.DoubleN_kidstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class AdminDashboardController {

    @Autowired private OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;

    @GetMapping("/admin/home")
    public String dashboard(
            @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth startMonth,
            @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth endMonth,
            Model model) {

        // 1. Xử lý khoảng thời gian lọc (Mặc định 6 tháng gần nhất nếu không chọn)
        if (startMonth == null) startMonth = YearMonth.now().minusMonths(5);
        if (endMonth == null) endMonth = YearMonth.now();

        Date startDate = Date.from(startMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endMonth.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        // 2. Lấy dữ liệu thống kê thật
        List<Order> filteredOrders = orderRepository.findSuccessfulOrdersBetween(startDate, endDate);

        double revenue = filteredOrders.stream().mapToDouble(Order::getTotalAmount).sum();
        long orderCount = filteredOrders.size();
        long customerCount = userRepository.countByRole("ROLE_CUSTOMER");
        long productCount = productRepository.count();

        // 3. Xử lý dữ liệu Biểu đồ (Chia theo từng tháng trong khoảng lọc)
        List<String> labels = new ArrayList<>();
        List<Double> chartData = new ArrayList<>();
        YearMonth temp = startMonth;
        while (!temp.isAfter(endMonth)) {
            labels.add("Tháng " + temp.getMonthValue() + "/" + temp.getYear());

            YearMonth finalTemp = temp;
            double monthRev = filteredOrders.stream()
                    .filter(o -> {
                        LocalDate ld = o.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return YearMonth.from(ld).equals(finalTemp);
                    })
                    .mapToDouble(Order::getTotalAmount).sum();

            chartData.add(monthRev / 1000000); // Đổi sang đơn vị Triệu cho dễ nhìn
            temp = temp.plusMonths(1);
        }

        // 4. Đơn hàng cần duyệt (Thật)
        List<Order> pendingOrders = orderRepository.findByStatusIgnoreCaseOrderByCreateDateDesc("pending");

        // 5. Top sản phẩm bán chạy (Thật)
        List<Product> topProducts = orderDetailRepository.findTopBestSellers(PageRequest.of(0, 4));

        model.addAttribute("revenue", revenue);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("customerCount", customerCount);
        model.addAttribute("productCount", productCount);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("chartLabels", labels);
        model.addAttribute("chartData", chartData);
        model.addAttribute("bestSellers", topProducts);
        model.addAttribute("start", startMonth);
        model.addAttribute("end", endMonth);

        return "admin/home";
    }
}