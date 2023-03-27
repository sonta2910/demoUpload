package com.example.demoupload1.controller;

import com.example.demoupload1.model.Product;
import com.example.demoupload1.model.ProductForm;
import com.example.demoupload1.service.ICoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/products")
public class ProductController {
    private int id = 0;
    @Value("${file-upload}")
    private String fileUpload;
    @Autowired
    private ICoreService<Product> iCoreService;

    @GetMapping
    public ModelAndView findAll() {
        ModelAndView modelAndView = new ModelAndView("product/list");
        modelAndView.addObject("products", iCoreService.findAll());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("product/create");
        modelAndView.addObject("productForm", new ProductForm());
        return modelAndView;
    }

    @PostMapping
    public String create(@ModelAttribute ProductForm productForm, Model model) {
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Product product = new Product(productForm.getId(), productForm.getName(),
                productForm.getDescription(), fileName);
        iCoreService.save(product);
        model.addAttribute("productForm", productForm);
        id++;
        return "redirect:/products";
    }

    @GetMapping("/update/{id}")
    public String updateForm(Model model, @PathVariable Integer id) {
        model.addAttribute("product", iCoreService.findById(id));
        return "product/update";
    }

    @PostMapping("/update")
    public String updatePicture(@ModelAttribute ProductForm productForm,
                                Model model) {
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Product product = new Product(productForm.getId(), productForm.getName(),
                productForm.getDescription(), fileName);
        iCoreService.update(product.getId(), product);
        model.addAttribute("productForm", productForm);
        return "redirect:/products";
    }
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        iCoreService.delete(id);
        return "redirect:/products";
    }
}

