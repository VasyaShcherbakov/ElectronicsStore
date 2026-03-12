/*
package com.OnlineElectronicsStore.OnlineElectronicsStore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ImageUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleImage(ImageUploadException ex, Model model) {
        model.addAttribute("title", "Помилка загрузки зображення");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("code", "IMG-001");
        model.addAttribute("backUrl", "/user/home/add");
        return "error";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProduct(ProductNotFoundException ex, Model model) {
        model.addAttribute("title", "Товар не знайдено");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("code", "PRD-404");
        model.addAttribute("backUrl", "/products");
        return "error";
    }


    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Model model) {
        model.addAttribute("title", "Сторінку не знайдено");
        model.addAttribute("message", "Запитуваний ресурс відсутній");
        model.addAttribute("code", "404");
        model.addAttribute("backUrl", "/");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAny(Exception ex, Model model) {
        ex.printStackTrace(); // логировать обязательно

        model.addAttribute("title", "Помилка в системі");
        model.addAttribute("message", "Щось пішло не так 😢");
        model.addAttribute("code", "SYS-500");
        model.addAttribute("backUrl", "/");
        return "error";
    }
}*/
