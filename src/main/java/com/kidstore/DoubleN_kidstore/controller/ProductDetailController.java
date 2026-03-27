package com.kidstore.DoubleN_kidstore.controller;

import com.kidstore.DoubleN_kidstore.model.Product;
import com.kidstore.DoubleN_kidstore.service.ProductService; // Import Service của bạn
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductDetailController {

    // Tiêm (Inject) ProductService để thao tác với Database
    @Autowired
    private ProductService productService;

    // Bắt URL chuẩn SEO: /product/ten-san-pham-khong-dau/id
    @GetMapping("/product/{slug}/{id}")
    public String showProductDetail(@PathVariable("slug") String slug, @PathVariable("id") Long id, Model model) {

        Product product = productService.findById(id);

        if (product == null) {
            return "redirect:/";
        }

        model.addAttribute("product", product);

        // THÊM ĐOẠN NÀY: Lấy 4 sản phẩm ngẫu nhiên (hoặc cùng danh mục) để hiển thị ở "Các sản phẩm khác"
        // Ở đây tôi lấy tạm tất cả rồi cắt ra 4 cái đầu tiên, nếu bạn có hàm lấy top 4 thì càng tốt
        List<Product> allProducts = productService.getAllProducts();
        List<Product> relatedProducts = allProducts.size() > 4 ? allProducts.subList(0, 4) : allProducts;
        model.addAttribute("relatedProducts", relatedProducts);

        return "product-detail";
    }

}