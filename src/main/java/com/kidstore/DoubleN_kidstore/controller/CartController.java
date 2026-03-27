package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.model.CartItem;
import com.kidstore.DoubleN_kidstore.model.Product;
import com.kidstore.DoubleN_kidstore.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ProductService productService;

    // Hiển thị trang giỏ hàng
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        double total = 0;
        for (CartItem item : cart) {
            total += item.getPrice() * item.getQuantity();
        }

        model.addAttribute("cartItems", cart);
        model.addAttribute("totalPrice", total);
        return "cart";
    }

    // API: Thêm vào giỏ hàng (Dùng cho AJAX để hiện popup)
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
        // Kiểm tra xem sản phẩm cùng màu, cùng size đã có chưa
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId) && item.getColor().equals(color) && item.getSize().equals(size)) {
                item.setQuantity(item.getQuantity() + quantity);
                isExist = true;
                break;
            }
        }

        // Nếu chưa có thì thêm mới
        if (!isExist) {
            cart.add(new CartItem(p.getId(), p.getName(), p.getPrice(), p.getImage(), color, size, quantity));
        }

        session.setAttribute("cart", cart);
        return ResponseEntity.ok().body("Thành công");
    }
}