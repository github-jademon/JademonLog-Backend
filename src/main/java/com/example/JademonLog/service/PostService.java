package com.example.JademonLog.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    ResponseEntity getList();

    ResponseEntity getPost(Long id);
}
