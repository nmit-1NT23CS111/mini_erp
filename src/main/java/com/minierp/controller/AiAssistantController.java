package com.minierp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AiAssistantController {

    @GetMapping("/assistant")
    public String showAssistant() {
        return "assistant";
    }
}
