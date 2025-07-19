package com.brunomessias.portfolio_java_fullstack.controller;

import com.brunomessias.portfolio_java_fullstack.dto.projeto.AtualizarProjetoDTO;
import com.brunomessias.portfolio_java_fullstack.dto.projeto.CriarProjetoDTO;
import com.brunomessias.portfolio_java_fullstack.entities.RiscoProjeto;
import com.brunomessias.portfolio_java_fullstack.entities.StatusProjeto;
import com.brunomessias.portfolio_java_fullstack.services.PessoaService;
import com.brunomessias.portfolio_java_fullstack.services.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projetos")
@RequiredArgsConstructor
@Tag(name = "Projetos", description = "Operações relacionadas a projetos")
class ProjetoController {

    private final ProjetoService projetoService;
    private final PessoaService pessoaService;

    @GetMapping("/novo")
    public String home(Model model) {
        model.addAttribute("gerentes", pessoaService.buscarGerentes());
        model.addAttribute("pessoas", pessoaService.buscarFuncionarios());
        model.addAttribute("riscos", RiscoProjeto.values());
        return "formProjeto";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("projeto", projetoService.buscar(id));
        model.addAttribute("gerentes", pessoaService.buscarGerentes());
        model.addAttribute("pessoas", pessoaService.buscarFuncionarios());
        model.addAttribute("riscos", RiscoProjeto.values());
        model.addAttribute("statusList", StatusProjeto.values());
        return "formProjeto";
    }

    @Operation(summary = "Cria um novo projeto")
    @PostMapping
    public String criar(@Valid @ModelAttribute CriarProjetoDTO dto,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            projetoService.criar(dto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Projeto criado com sucesso!");
            return "redirect:/";

        } catch (ResponseStatusException e) {
            model.addAttribute("erro", e.getReason());
            model.addAttribute("gerentes", pessoaService.buscarGerentes());
            model.addAttribute("pessoas", pessoaService.buscarFuncionarios());
            model.addAttribute("riscos", RiscoProjeto.values());
            return "formProjeto";
        }
    }

    @Operation(summary = "Atualiza um projeto")
    @PutMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute AtualizarProjetoDTO dto,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        try {
            projetoService.atualizar(id, dto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Projeto atualizado com sucesso!");
            return "redirect:/";

        } catch (ResponseStatusException e) {
            model.addAttribute("erro", e.getReason());
            model.addAttribute("projeto", dto);
            model.addAttribute("gerentes", pessoaService.buscarGerentes());
            model.addAttribute("pessoas", pessoaService.buscarFuncionarios());
            model.addAttribute("riscos", RiscoProjeto.values());
            model.addAttribute("statusList", StatusProjeto.values());
            return "formProjeto";
        }
    }

    @Operation(summary = "Deleta um projeto")
    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            projetoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Projeto excluído com sucesso!");
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getReason());
        }
        return "redirect:/";
    }

}