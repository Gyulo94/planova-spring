package com.planova.server.image.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.planova.server.image.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("image")
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;

  @PostMapping("upload")
  public List<String> uploadImages(@RequestParam("files") MultipartFile[] files) {
    List<String> response = imageService.uploadImages(files);
    return response;
  }
}
