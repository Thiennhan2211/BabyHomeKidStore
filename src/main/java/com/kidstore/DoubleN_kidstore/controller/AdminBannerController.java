package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.entity.Banner;
import com.kidstore.DoubleN_kidstore.repository.BannerRepository;
import com.kidstore.DoubleN_kidstore.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class AdminBannerController {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    // Load trang quản lý Banner
    @GetMapping("/admin/banner")
    public String bannerPage(Model model) {
        model.addAttribute("banners", bannerRepository.findAll());
        return "admin/banner";
    }

    // Thêm Banner mới
    @PostMapping("/admin/banner/add")
    public String addBanner(@RequestParam("imageFile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(file);
            Banner banner = new Banner();
            banner.setImage(imageUrl); // Lưu link Cloudinary
            bannerRepository.save(banner);
        }
        return "redirect:/admin/banner";
    }

    // Xoá Banner
    @GetMapping("/admin/banner/delete/{id}")
    public String deleteBanner(@PathVariable Long id) {
        bannerRepository.deleteById(id);
        return "redirect:/admin/banner";
    }
}