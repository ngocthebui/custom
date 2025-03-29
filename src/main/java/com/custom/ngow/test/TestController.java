package com.custom.ngow.test;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin("*")
public class TestController {

    @GetMapping("/home")
    public String test(Model model) {
        List<Product> featuredProducts = Arrays.asList(
                new Product(1L, "Túi xách thời trang nữ", 850000, "/images/lingbao1.jpg"),
                new Product(2L, "Túi đeo chéo da thật", 1250000, "/images/lingbao2.jpg"),
                new Product(3L, "Balo du lịch cao cấp", 1500000, "/images/lingbao3.jpg"),
                new Product(4L, "Túi xách công sở", 950000, "/images/lingbao4.jpg"),
                new Product(4L, "Túi xách du lịch", 950000, "/images/lingbao5.jpg")
        );

        model.addAttribute("newProducts", featuredProducts);
        model.addAttribute("cartItemCount", 0);
        return "views/home";
    }

    @GetMapping("/trans")
    public String trans() {
        return "auth/login";
    }

}
