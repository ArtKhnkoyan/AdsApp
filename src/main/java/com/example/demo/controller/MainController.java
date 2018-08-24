package com.example.demo.controller;

import com.example.demo.model.Ads;
import com.example.demo.model.Category;
import com.example.demo.repository.AdsRepository;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    private final CategoryRepository categoryRepository;
    private final AdsRepository adsRepository;

    @Autowired
    public MainController(CategoryRepository categoryRepository, AdsRepository adsRepository) {
        this.categoryRepository = categoryRepository;
        this.adsRepository = adsRepository;
    }

    @GetMapping("/")
    public String main(ModelMap modelMap) {
        List<Category> categoryList = categoryRepository.findAll();
        modelMap.addAttribute("categories", categoryList);
        List<Ads> adsList = adsRepository.findAll();
        modelMap.addAttribute("ads", adsList);
        return "index";
    }

    @GetMapping("/admin")
    public String admin(ModelMap modelMap) {
        List<Category> categoryList = categoryRepository.findAll();
        modelMap.addAttribute("categories", categoryList);
        return "admin";
    }
}
