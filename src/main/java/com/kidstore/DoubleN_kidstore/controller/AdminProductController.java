package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.model.Product;
import com.kidstore.DoubleN_kidstore.repository.ProductRepository;
import com.kidstore.DoubleN_kidstore.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
public class AdminProductController {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    // Tự tạo một danh sách Category bằng chữ để truyền xuống giao diện (Thay cho cái Bảng rác đã xóa)
    List<String> categories = Arrays.asList("shirt", "pants", "dress", "set", "jacket", "shoes");

    // LOAD PAGE
    @GetMapping("/admin/product")
    public String product(Model model){
        model.addAttribute("products", productRepository.findAll());

        // Truyền danh sách chữ xuống HTML
        model.addAttribute("categories", categories);
        return "admin/product";
    }

    // ADD
    // ADD SẢN PHẨM MỚI
    @PostMapping("/admin/product/add")
    public String add(Product product,
                      @RequestParam(value = "categoryId", required = false) String categoryId,
                      @RequestParam(value = "category", required = false) String category,
                      @RequestParam(value = "imageFile", required = false) MultipartFile file) throws IOException {

        String finalCategory = (category != null && !category.isEmpty()) ? category : categoryId;
        product.setCategory(finalCategory);

        // Nếu admin có chọn file ảnh
        if (file != null && !file.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(file);
            product.setImage(imageUrl); // Lưu link web vào Database
        }

        productRepository.save(product);
        return "redirect:/admin/product";
    }

    // CẬP NHẬT SẢN PHẨM
    @PostMapping("/admin/product/update")
    public String update(Product product,
                         @RequestParam(value = "categoryId", required = false) String categoryId,
                         @RequestParam(value = "category", required = false) String category,
                         @RequestParam(value = "imageFile", required = false) MultipartFile file) throws IOException {

        String finalCategory = (category != null && !category.isEmpty()) ? category : categoryId;
        product.setCategory(finalCategory);

        // Nếu admin CHỌN ẢNH MỚI thì up lên mây và đè link mới
        if (file != null && !file.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(file);
            product.setImage(imageUrl);
        }
        // Nếu không chọn ảnh mới, Spring Boot sẽ tự giữ lại link ảnh cũ nhờ thẻ input hidden ở HTML

        productRepository.save(product);
        return "redirect:/admin/product";
    }

    // DELETE
    @GetMapping("/admin/product/delete/{id}")
    public String delete(@PathVariable Long id){
        productRepository.deleteById(id);
        return "redirect:/admin/product";
    }
    @PostMapping("/admin/product/save")
    public String saveProduct(@RequestParam("imageFile") MultipartFile file, Product product) throws IOException {

        // Nếu admin có chọn file ảnh mới
        if (!file.isEmpty()) {
            // Bắn file lên Cloudinary, lấy link URL về
            String imageUrl = cloudinaryService.uploadImage(file);

            // Lưu cái link HTTPS đó vào Database thay vì tên file cũ
            product.setImage(imageUrl);
        }

        // Lưu sản phẩm vào DB
        productRepository.save(product);

        return "redirect:/admin/product"; // Quay lại trang danh sách SP
    }
}