package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.model.Product;
import com.kidstore.DoubleN_kidstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class AdminProductController {

    @Autowired
    ProductRepository productRepository;

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
    @PostMapping("/admin/product/add")
    public String add(Product product,
                      @RequestParam(value = "categoryId", required = false) String categoryId,
                      @RequestParam(value = "category", required = false) String category) {

        // Đề phòng HTML của bạn dùng name="categoryId" hay name="category" thì máy chủ đều bắt được hết
        String finalCategory = (category != null && !category.isEmpty()) ? category : categoryId;
        product.setCategory(finalCategory);

        productRepository.save(product);
        return "redirect:/admin/product";
    }

    // UPDATE
    @PostMapping("/admin/product/update")
    public String update(Product product,
                         @RequestParam(value = "categoryId", required = false) String categoryId,
                         @RequestParam(value = "category", required = false) String category) {

        String finalCategory = (category != null && !category.isEmpty()) ? category : categoryId;
        product.setCategory(finalCategory);

        productRepository.save(product);
        return "redirect:/admin/product";
    }

    // DELETE
    @GetMapping("/admin/product/delete/{id}")
    public String delete(@PathVariable Long id){
        productRepository.deleteById(id);
        return "redirect:/admin/product";
    }
}