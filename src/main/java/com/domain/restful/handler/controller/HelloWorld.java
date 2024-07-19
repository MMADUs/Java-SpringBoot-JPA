package com.domain.restful.handler.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class HelloWorld {

  @GetMapping("/hello")
  public String getMethodName(@RequestParam String param) {
    return "hello world " + param;
  }
  
}
