package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.model.Product;
import com.kidstore.DoubleN_kidstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.File;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model){

        // tất cả sản phẩm
        model.addAttribute("products", productService.getAllProducts());

        // bé gái
        model.addAttribute("girlProducts", productService.getByGender("girl"));

        // bé trai
        model.addAttribute("boyProducts", productService.getByGender("boy"));

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
    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }
}