package com.example.JademonLog.controller;

import com.example.JademonLog.entity.post.Post;
import com.example.JademonLog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PostMapping("/post/image") //
    public ResponseEntity SchoolClubImage(@RequestPart Post request,
                                                         @RequestPart MultipartFile multipartFile) throws IOException {
        return postService.savePost(request, multipartFile);
    }
}
