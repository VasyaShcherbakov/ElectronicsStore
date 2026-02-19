package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;


import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.CategoryService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ProductServiceImpl;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@Controller
@RequestMapping("/user/home")
public class UserController {

    private static final Logger log =
            LoggerFactory.getLogger(UserController.class);


    private final ProductServiceImpl productService;
    private final CategoryService categoryService;
    private final UserService userService;

    public UserController(ProductServiceImpl productService, CategoryService categoryService, UserService userService ) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
    }



    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        productService.addProduct(
                product,
                imageFile,
                userDetails.getUsername()
        );

        return "redirect:/user/home/main";
    }



    @GetMapping("/main")
    public String userHome(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam(value = "query", required = false) String query,
                           Model model) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(userDetails.getUsername());

        if (user == null) {
            return "redirect:/login";
        }

        List<Product> products = productService.getProductsForUser(user, query);

        model.addAttribute("products", products);
        model.addAttribute("user", user);
        model.addAttribute("query", query);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("product", new Product());

        return "user-home";
    }

}
