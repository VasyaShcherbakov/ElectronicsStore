package com.OnlineElectronicsStore.OnlineElectronicsStore.exception;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ImageUploadException.class)
    public String handleImage(ImageUploadException ex, Model model) {
        model.addAttribute("title", "Ошибка загрузки изображения");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("code", "IMG-001");
        model.addAttribute("backUrl", "/user/home/add");
        return "error";
    }

    /*@ExceptionHandler(ProductNotFoundException.class)
    public String handleProduct(ProductNotFoundException ex, Model model) {
        model.addAttribute("title", "Товар не найден");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("code", "PRD-404");
        model.addAttribute("backUrl", "/products");
        return "error";
    }*/

    @ExceptionHandler(Exception.class)
    public String handleAny(Exception ex, Model model) {
        model.addAttribute("title", "Системная ошибка");
        model.addAttribute("message", "Что-то пошло не так 😢");
        model.addAttribute("code", "SYS-500");
        model.addAttribute("backUrl", "/");
        return "error";
    }
}
