package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.model.Category;
import com.kidstore.DoubleN_kidstore.model.Product;
import com.kidstore.DoubleN_kidstore.repository.CategoryRepository;
import com.kidstore.DoubleN_kidstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    // LOAD PAGE
    @GetMapping("/admin/product")
    public String product(Model model){
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product";
    }

    // ADD
    @PostMapping("/admin/product/add")
    public String add(Product product, @RequestParam Long categoryId){

        Category c = categoryRepository.findById(categoryId).orElse(null);
        product.setCategory(c);

        productRepository.save(product);

        return "redirect:/admin/product";
    }

    // UPDATE
    @PostMapping("/admin/product/update")
    public String update(Product product, @RequestParam Long categoryId){

        Category c = categoryRepository.findById(categoryId).orElse(null);
        product.setCategory(c);

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