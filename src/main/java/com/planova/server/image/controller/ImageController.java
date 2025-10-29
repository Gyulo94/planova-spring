package com.planova.server.image.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.planova.server.image.service.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("image")
@RequiredArgsConstructor
@Tag(name = "이미지 업로드", description = "이미지 업로드 관련 API")
public class ImageController {

  private final ImageService imageService;

  @Operation(summary = "이미지 업로드", description = "여러 이미지를 업로드합니다.")
  @PostMapping("upload")
  public List<String> uploadImages(@RequestParam("files") MultipartFile[] files) {
    List<String> response = imageService.uploadImages(files);
    return response;
  }
}
