package com.brunomessias.portfolio_java_fullstack.controller;

import com.brunomessias.portfolio_java_fullstack.services.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProjetoService projetoService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("projetos", projetoService.listarTodos());
        return "index";
    }
}