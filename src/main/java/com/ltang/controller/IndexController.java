package com.ltang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * index controller
 *
 * @author tangliu
 */
@Controller
public class IndexController {

    /**
     * redirect to index page
     *
     * @return
     */
    @GetMapping("/")
    public String redirectToIndex() {
        return "redirect:/index";
    }

    /**
     * index page
     *
     * @return
     */
    @GetMapping("/index")
    public ModelAndView showIndex() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }
} 