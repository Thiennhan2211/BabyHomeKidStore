package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.entity.Order;
import com.kidstore.DoubleN_kidstore.entity.OrderDetail;
import com.kidstore.DoubleN_kidstore.entity.User;
import com.kidstore.DoubleN_kidstore.model.CartItem;
import com.kidstore.DoubleN_kidstore.model.Product;
import com.kidstore.DoubleN_kidstore.service.ProductService;
import com.kidstore.DoubleN_kidstore.repository.OrderRepository;
import com.kidstore.DoubleN_kidstore.repository.OrderDetailRepository;
import com.kidstore.DoubleN_kidstore.repository.UserRepository;
import com.kidstore.DoubleN_kidstore.entity.Voucher;
import com.kidstore.DoubleN_kidstore.repository.VoucherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;
    @Autowired private ProductService productService;
    @Autowired private VoucherRepository voucherRepository;

    // ----- PHẦN 1: MỞ TRANG XÁC NHẬN THANH TOÁN -----
    @GetMapping("/checkout")
    public String showCheckoutPage(@RequestParam(value = "voucher", required = false) String voucherCode,
                                   HttpSession session, Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
        if (cartItems == null || cartItems.isEmpty()) return "redirect:/cart";

        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);

        // Tính tổng tiền gốc
        double total = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        double discount = 0;
        String appliedVoucher = "";

        // Kiểm tra Voucher xem có hợp lệ và đủ điều kiện không
        if (voucherCode != null && !voucherCode.isEmpty()) {
            List<Voucher> allVouchers = voucherRepository.findAll();
            Voucher v = allVouchers.stream().filter(x -> x.getCode().equals(voucherCode)).findFirst().orElse(null);

            if (v != null && total >= v.getMinAmount()) {
                discount = v.getDiscount();
                appliedVoucher = v.getCode();
            }
        }

        // Tính giá cuối cùng
        double finalPrice = total - discount;
        if (finalPrice < 0) finalPrice = 0;

        // Gửi toàn bộ thông tin xuống HTML
        model.addAttribute("totalPrice", total);
        model.addAttribute("discount", discount);
        model.addAttribute("finalPrice", finalPrice);
        model.addAttribute("appliedVoucher", appliedVoucher);

        return "checkout";
    }

    // ----- PHẦN 2: KHÁCH HÀNG ĐẶT HÀNG (CHỐT ĐƠN) -----
    @PostMapping("/checkout")
    public String processCheckout(@RequestParam(value = "note", required = false) String note,
                                  @RequestParam(value = "address", defaultValue = "Địa chỉ mặc định") String address,
                                  @RequestParam(value = "phone", defaultValue = "0999999999") String phone,
                                  @RequestParam(value = "voucherCode", required = false) String voucherCode,
                                  HttpSession session, Principal principal, RedirectAttributes redirectAttributes) {

        if (principal == null) return "redirect:/login";

        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
        if (cartItems == null || cartItems.isEmpty()) return "redirect:/cart";

        User user = userRepository.findByUsername(principal.getName());
        double total = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        double discount = 0;

        // Khấu trừ Voucher trước khi lưu
        if (voucherCode != null && !voucherCode.isEmpty()) {
            List<Voucher> allVouchers = voucherRepository.findAll();
            Voucher v = allVouchers.stream().filter(x -> x.getCode().equals(voucherCode)).findFirst().orElse(null);
            if (v != null && total >= v.getMinAmount()) {
                discount = v.getDiscount();

                // Đánh dấu khách đã xài mã này (Để xóa khỏi trang Profile)
                String currentUsed = user.getUsedVouchers() == null ? "" : user.getUsedVouchers();
                if (!currentUsed.contains(voucherCode)) {
                    user.setUsedVouchers(currentUsed + voucherCode + ",");
                    userRepository.save(user);
                }
            }
        }

        double finalPrice = total - discount;
        if (finalPrice < 0) finalPrice = 0;

        // TẠO ĐƠN HÀNG VỚI GIÁ ĐÃ TRỪ VOUCHER
        Order order = new Order();
        order.setCustomer(user);
        order.setAddress(address);
        order.setPhone(phone);
        order.setTotalAmount(finalPrice); // Quan trọng: Lưu giá đã giảm!
        order.setStatus("pending");
        order.setCreateDate(new Date());

        orderRepository.save(order);

        // Lưu chi tiết từng món đồ...
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            Product p = productService.findById(item.getProductId());
            detail.setProduct(p);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());
            detail.setColor(item.getColor());
            detail.setSize(item.getSize());
            orderDetailRepository.save(detail);
        }

        session.removeAttribute("cart");
        redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
        return "redirect:/profile";
    }

    // ----- PHẦN 3: HIỂN THỊ TRANG QUẢN LÝ ĐƠN HÀNG (ADMIN) -----
    @GetMapping("/admin/order")
    public String showAdminOrderPage(Model model) {
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "order";
    }

    // ----- PHẦN 4: ADMIN LẤY CHI TIẾT ĐƠN HÀNG ĐỂ HIỆN POPUP (AJAX) -----
    @GetMapping("/admin/order/details/{id}")
    @ResponseBody
    public List<Map<String, Object>> getOrderDetails(@PathVariable("id") Long id) {
        List<OrderDetail> details = orderDetailRepository.findByOrderId(id);
        return details.stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", d.getProduct().getId());
            map.put("productName", d.getProduct().getName());
            map.put("quantity", d.getQuantity());
            map.put("price", d.getPrice());
            return map;
        }).collect(Collectors.toList());
    }

    // Admin Duyệt (APPROVED) hoặc Hủy (CANCELED) đơn hàng
    @PostMapping("/admin/order/status/{id}")
    @ResponseBody
    public ResponseEntity<?> updateOrderStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build(); // Trả về lỗi nếu không tìm thấy đơn
    }
}