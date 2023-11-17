package com.example.JademonLog.service;

import com.example.JademonLog.entity.post.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface PostService {
    ResponseEntity getList();

    ResponseEntity getPost(Long id);

    ResponseEntity savePost(Post request, MultipartFile multipartFile) throws IOException;
}
