package com.custom.ngow.shop.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/upload")
public class GoogleDriveUploadPage {

    @GetMapping
    public String upload() {
        return "views/google/index";
    }
}
