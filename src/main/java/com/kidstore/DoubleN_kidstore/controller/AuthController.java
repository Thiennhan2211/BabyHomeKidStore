package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.entity.User;
import com.kidstore.DoubleN_kidstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // 1. Hiển thị trang Đăng nhập
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // 2. Hiển thị trang Đăng ký
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // 3. Nhận dữ liệu từ form Đăng ký và lưu xuống MySQL
    @PostMapping("/register")
    public String processRegister(@RequestParam("username") String username,
                                  @RequestParam("email") String email,
                                  @RequestParam("password") String password,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  RedirectAttributes redirectAttributes) {

        // Kiểm tra 2 mật khẩu có khớp nhau không
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu nhập lại không khớp!");
            return "redirect:/register";
        }

        // Kiểm tra xem Username đã bị người khác đăng ký chưa
        if (userRepository.findByUsername(username) != null) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại! Vui lòng chọn tên khác.");
            return "redirect:/register";
        }

        // Tạo tài khoản mới
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);

        // ÉP CỨNG QUYỀN CUSTOMER (Chỉ có thể tạo tài khoản user thường từ web)
        newUser.setRole("ROLE_CUSTOMER");

        // Lưu thẳng vào Database
        userRepository.save(newUser);

        // Báo thành công và đá về trang Login
        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Hãy đăng nhập ngay.");
        return "redirect:/login";
    }
}