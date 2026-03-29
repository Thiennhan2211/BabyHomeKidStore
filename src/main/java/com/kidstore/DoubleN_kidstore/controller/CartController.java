package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.entity.User;
import com.kidstore.DoubleN_kidstore.model.CartItem;
import com.kidstore.DoubleN_kidstore.model.Product;
import com.kidstore.DoubleN_kidstore.repository.UserRepository;
import com.kidstore.DoubleN_kidstore.repository.VoucherRepository;
import com.kidstore.DoubleN_kidstore.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired private ProductService productService;
    @Autowired private VoucherRepository voucherRepository;
    @Autowired private UserRepository userRepository;

    // Hiển thị trang giỏ hàng
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model, Principal principal) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        double total = 0;
        for (CartItem item : cart) {
            total += item.getPrice() * item.getQuantity();
        }

        // Truyền User xuống để HTML biết khách đã xài mã nào chưa
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName());
            model.addAttribute("user", user);
        }

        model.addAttribute("cartItems", cart);
        model.addAttribute("totalPrice", total);

        // 👉 DÒNG QUAN TRỌNG NHẤT: Bơm Voucher từ Database xuống giao diện
        model.addAttribute("vouchers", voucherRepository.findAll());

        return "cart";
    }

    // API: Thêm vào giỏ hàng
    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseEntity<?> addToCartApi(@RequestParam Long productId,
                                          @RequestParam(defaultValue = "1") int quantity,
                                          @RequestParam(required = false, defaultValue = "Mặc định") String color,
                                          @RequestParam(required = false, defaultValue = "Freesize") String size,
                                          HttpSession session) {
        Product p = productService.findById(productId);
        if (p == null) return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy sản phẩm!");

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        boolean isExist = false;
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId) && item.getColor().equals(color) && item.getSize().equals(size)) {
                item.setQuantity(item.getQuantity() + quantity);
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            cart.add(new CartItem(p.getId(), p.getName(), p.getPrice(), p.getImage(), color, size, quantity));
        }
        session.setAttribute("cart", cart);
        return ResponseEntity.ok().body("Thành công");
    }

    // API: Xóa khỏi giỏ hàng
    @PostMapping("/api/cart/remove")
    public ResponseEntity<?> removeFromCart(@RequestParam("productId") Long productId,
                                            @RequestParam("color") String color,
                                            @RequestParam("size") String size,
                                            HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(item -> item.getProductId().equals(productId)
                    && color.equals(item.getColor())
                    && size.equals(item.getSize()));
            session.setAttribute("cart", cart);
        }
        return ResponseEntity.ok().build();
    }

    // API: Cập nhật số lượng
    @PostMapping("/api/cart/update")
    public ResponseEntity<?> updateCartQty(@RequestParam("productId") Long productId,
                                           @RequestParam("color") String color,
                                           @RequestParam("size") String size,
                                           @RequestParam("qty") int qty,
                                           HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null && qty > 0) {
            for (CartItem item : cart) {
                if (item.getProductId().equals(productId) && color.equals(item.getColor()) && size.equals(item.getSize())) {
                    item.setQuantity(qty);
                    break;
                }
            }
            session.setAttribute("cart", cart);
        }
        return ResponseEntity.ok().build();
    }
}