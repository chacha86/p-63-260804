package com.back.p63260804;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 브라우저를 통해 해당 클래스의 함수를 호출할 수 잇따.
public class MainController {

    @GetMapping("/")
    public void index() {
        System.out.println("index() 메서드 호출됨!");
    }

    @GetMapping("/hello")
    public void hello() {
        System.out.println("hello() 메서드 호출됨!");
    }


}