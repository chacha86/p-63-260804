package com.back.p63260804;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // 브라우저를 통해 해당 클래스의 함수를 호출할 수 잇따.
public class MainController {

    @GetMapping("/")
    @ResponseBody
    public String index() {
        System.out.println("index() 메서드 호출됨!");
        return "index() 메서드 호출됨!";
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        System.out.println("hello() 메서드 호출됨!"); // 서버 콘솔
        // 고객(요청한 쪽)의 브라우저에 출력
        return "hello() 메서드 호출됨!";
    }


}