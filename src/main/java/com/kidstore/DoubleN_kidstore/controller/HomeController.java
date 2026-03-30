package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.entity.User;
import com.kidstore.DoubleN_kidstore.entity.Order; // IMPORT THÊM ORDER
import com.kidstore.DoubleN_kidstore.model.Product;
import com.kidstore.DoubleN_kidstore.repository.UserRepository;
import com.kidstore.DoubleN_kidstore.repository.OrderRepository; // IMPORT THÊM ORDER REPOSITORY
import com.kidstore.DoubleN_kidstore.service.CloudinaryService;
import com.kidstore.DoubleN_kidstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    // THÊM ORDER REPOSITORY VÀO ĐÂY ĐỂ TÌM ĐƠN HÀNG
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private com.kidstore.DoubleN_kidstore.repository.VoucherRepository voucherRepository;


    @GetMapping("/")
    public String home(Model model){

        // tất cả sản phẩm
        model.addAttribute("products", productService.getAllProducts());

        // bé gái
        model.addAttribute("girlProducts", productService.getByGender("girl"));

        // bé trai
        model.addAttribute("boyProducts", productService.getByGender("boy"));
        // Lấy 8 sản phẩm bán chạy nhất truyền xuống giao diện
        model.addAttribute("bestSellers", productService.getBestSellers(8));

        // banner
        try {
            File folder = new ClassPathResource("static/images").getFile();

            String[] banners = folder.list((dir, name) ->
                    name.startsWith("banner") &&
                            (name.endsWith(".jpg") || name.endsWith(".png"))
            );

            model.addAttribute("banners", banners);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "index";
    }

    // API xử lý Tìm kiếm
    @GetMapping("/search")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model) {
        // Gọi Service tìm sản phẩm theo từ khóa
        List<Product> searchResults = productService.searchProducts(keyword);

        // Tái sử dụng lại giao diện trang chủ để hiển thị kết quả
        // Đổ kết quả vào biến 'products' (khối Sản phẩm mới trên index.html sẽ hiển thị chúng)
        model.addAttribute("products", searchResults);
        model.addAttribute("keyword", keyword);

        // Truyền rỗng cho các list khác để ẩn chúng đi trên trang kết quả tìm kiếm
        model.addAttribute("girlProducts", null);
        model.addAttribute("boyProducts", null);

        return "index";
    }

    // 1. Load trang Profile kèm dữ liệu cũ của User VÀ DANH SÁCH ĐƠN MUA
    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);

        List<com.kidstore.DoubleN_kidstore.entity.Order> myOrders = orderRepository.findByCustomerOrderByIdDesc(user);
        model.addAttribute("myOrders", myOrders);

        // 👉 LẠI LÀ DÒNG QUAN TRỌNG: Bơm Voucher xuống giao diện Profile
        model.addAttribute("vouchers", voucherRepository.findAll());

        return "profile";
    }

    // 2. Lưu Form Thông tin tài khoản
    @PostMapping("/profile/update-info")
    public String updateProfileInfo(@RequestParam("fullname") String fullname,
                                    @RequestParam("email") String email,
                                    @RequestParam("dob") String dob,
                                    @RequestParam("gender") String gender,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUsername(principal.getName());
        user.setFullname(fullname);
        user.setEmail(email);
        user.setDob(dob);
        user.setGender(gender);
        userRepository.save(user); // Lưu đè xuống Database

        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        return "redirect:/profile";
    }

    // 3. Lưu Form Địa chỉ
    @PostMapping("/profile/update-address")
    public String updateProfileAddress(@RequestParam("phone") String phone,
                                       @RequestParam("province") String province,
                                       @RequestParam("district") String district,
                                       @RequestParam("ward") String ward,
                                       @RequestParam("specific") String specific,
                                       Principal principal,
                                       RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUsername(principal.getName());

        // Nối các ô địa chỉ nhỏ thành 1 chuỗi dài để lưu cho tiện
        String fullAddress = specific + ", " + ward + ", " + district + ", " + province;

        user.setPhone(phone);
        user.setAddress(fullAddress);
        userRepository.save(user); // Lưu xuống Database

        redirectAttributes.addFlashAttribute("success", "Cập nhật địa chỉ thành công!");
        return "redirect:/profile";
    }
    // ===== TRANG DANH MỤC SẢN PHẨM (COLLECTION) =====
    @GetMapping("/collection")
    public String showCollection(@RequestParam(required = false) String gender,
                                 @RequestParam(required = false) String category,
                                 @RequestParam(required = false) String type,
                                 Model model) {

        // Lấy tất cả sản phẩm ra trước
        List<Product> products = productService.getAllProducts();
        String pageTitle = "Tất Cả Sản Phẩm";

        // Lọc theo "Bộ sưu tập mới" (Đảo ngược danh sách để lấy đồ mới nhất)
        if ("new".equals(type)) {
            java.util.Collections.reverse(products);
            pageTitle = "BỘ SƯU TẬP MỚI";
        } else {
            // Lọc theo giới tính (Bé trai / Bé gái)
            if (gender != null && !gender.isEmpty()) {
                products = products.stream().filter(p -> gender.equals(p.getGender())).toList();
                pageTitle = gender.equals("girl") ? "THỜI TRANG BÉ GÁI" : "THỜI TRANG BÉ TRAI";
            }

            // Lọc theo loại đồ (Giày dép, đầm váy...)
            if (category != null && !category.isEmpty()) {
                products = products.stream().filter(p -> category.equals(p.getCategory())).toList();
                if (gender == null) pageTitle = "DANH MỤC: " + category.toUpperCase();
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("pageTitle", pageTitle);
        return "collection";
    }

}