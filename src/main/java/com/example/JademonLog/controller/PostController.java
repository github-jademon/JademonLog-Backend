package com.example.JademonLog.controller;

import com.example.JademonLog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Log4j2
public class PostController {
    private final PostService postService;

    @GetMapping("/list")
    public ResponseEntity getList() {
        return postService.getList();
    }

    @GetMapping("/post/{id}")
    public ResponseEntity getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }
}
