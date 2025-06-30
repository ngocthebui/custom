package com.custom.ngow.shop.page;

import com.custom.ngow.shop.entity.HomeSlide;
import com.custom.ngow.shop.service.HomeSlideService;
import com.custom.ngow.shop.service.HomeVideoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

  private final HomeVideoService homeVideoService;
  private final HomeSlideService homeSlideService;

  @GetMapping
  public String home(Model model) {
    List<HomeSlide> featuredProducts = homeSlideService.getActiveHomeSlides();

    model.addAttribute("newProducts", featuredProducts);
    model.addAttribute("homeVideo", homeVideoService.getActiveHomeVideo());
    return "views/home";
  }

}
