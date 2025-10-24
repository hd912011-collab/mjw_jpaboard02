package org.mjw.jpaboard02.controller;

import org.mjw.jpaboard02.dto.SampleDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleRestController {
    @GetMapping("/test")
    public String test(){
        return "test";
    }
    @GetMapping("/test2")
    public SampleDTO test2(){
        SampleDTO sampleDTO = SampleDTO.builder()
                .id(1L)
                .name("test")
                .age(20)
                .address("부산시 부산진구")
                .build();
        return sampleDTO;
    }
}
