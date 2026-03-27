package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.model.Category;
import com.kidstore.DoubleN_kidstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    CategoryRepository repo;

    // LOAD PAGE
    @GetMapping
    public String viewCategory(Model model){
        model.addAttribute("categories", repo.findAll());
        return "admin/category";
    }

    // ADD
    @PostMapping("/add")
    public String add(Category c){
        c.setId(null); // auto tăng
        repo.save(c);
        return "redirect:/admin/category";
    }

    // UPDATE
    @PostMapping("/update")
    public String update(Category c){
        if(repo.existsById(c.getId())){
            repo.save(c);
        }
        return "redirect:/admin/category";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){   // ✅ FIX ở đây
        if(repo.existsById(id)){
            repo.deleteById(id);
        }
        return "redirect:/admin/category";
    }
}