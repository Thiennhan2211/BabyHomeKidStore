package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.entity.Voucher;
import com.kidstore.DoubleN_kidstore.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/voucher")
public class AdminVoucherController {

    @Autowired
    private VoucherRepository voucherRepository;

    // Hiển thị trang quản lý
    @GetMapping
    public String showVoucherPage(Model model) {
        List<Voucher> vouchers = voucherRepository.findAll();
        model.addAttribute("vouchers", vouchers);
        return "admin/voucher";
    }

    // Thêm hoặc Cập nhật Voucher
    @PostMapping("/save")
    public String saveVoucher(@ModelAttribute Voucher voucher) {
        voucherRepository.save(voucher);
        return "redirect:/admin/voucher";
    }

    // Xóa Voucher
    @GetMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable Long id) {
        voucherRepository.deleteById(id);
        return "redirect:/admin/voucher";
    }
}