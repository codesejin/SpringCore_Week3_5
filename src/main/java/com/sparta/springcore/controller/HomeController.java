package com.sparta.springcore.controller;

import com.sparta.springcore.model.UserRoleEnum;
import com.sparta.springcore.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    //홈페이지를 동적으로 만들어줌(유저네임 표시) : 템플릿엔진사용
    @GetMapping("/")
    //@AuthenticationPrincipal를 사용하면 로그인된 사용자가 넘어옴
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("username", userDetails.getUsername());
        //로그인한 사용자가 관리자인 경우에만 설정
        if (userDetails.getUser().getRole() == UserRoleEnum.ADMIN) {
            model.addAttribute("admin_role", true);
        }
        return "index";
    }
}