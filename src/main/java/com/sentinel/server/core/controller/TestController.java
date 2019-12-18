package com.sentinel.server.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author licong
 * @date 2019-12-18 3:22 下午
 */
@RestController
@RequestMapping("/")
public class TestController {

    @GetMapping("/test")
    public Integer publish() {
        return 123;
    }

}
