package com.example.JademonLog.service;

import com.example.JademonLog.entity.image.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ImageService {

    List<Image> saveImage(List<MultipartFile> multipartFiles) throws IOException;

}
