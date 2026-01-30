package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.ProductCategory;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;

    public AdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String categories(Model model, HttpServletRequest request) {
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("category", new ProductCategory());

        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", token);

        return "admin/categories";
    }


    @PostMapping("/add")
    public String hardcodedCategory() {
        System.out.println(">>> ADMIN ADD CALLED");
        categoryService.create("HARDCODE_TEST");
      
        return "redirect:/admin/categories";

    }



}
