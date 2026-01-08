package com.example.urlshortener.controller;

import com.example.urlshortener.dto.UrlManagementDto;
import com.example.urlshortener.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping(value={"", "/"})
    public String urlList(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable,
            Model model
    ) {
        Page<UrlManagementDto> urls = adminService.findAllUrls(pageable);
        model.addAttribute("urls" ,urls);
        return "admin/list";
    }

    @PostMapping("/{id}/delete")
    public String deleteUrl(@PathVariable Long id){
        adminService.deleteUrl(id);
        return "redirect:/admin";
    }

}
