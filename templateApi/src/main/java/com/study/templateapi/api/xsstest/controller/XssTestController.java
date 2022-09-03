package com.study.templateapi.api.xsstest.controller;

import com.study.templateapi.api.xsstest.dto.XssTestDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xss")
public class XssTestController {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public XssTestDto xssTest1(XssTestDto xssTestDto) {
        return xssTestDto;
    }
}
