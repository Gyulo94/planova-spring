package com.planova.server.image.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.exception.ApiException;
import com.planova.server.image.entity.EntityType;
import com.planova.server.image.entity.Image;
import com.planova.server.image.repository.ImageRepository;
import com.planova.server.image.request.ImageRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

  private final ImageRepository imageRepository;
  private final WebClient webClient;

  Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

  @Override
  public List<Image> findImagesByEntityId(UUID entityId, EntityType entityType) {
    return imageRepository.findByEntityIdAndEntityType(entityId, entityType);
  }

  @Override
  public List<String> uploadImages(MultipartFile[] files) {
    LOGGER.info("=== NestJS 서버로 파일 업로드 시작 ===");
    LOGGER.info("업로드할 파일 개수: {}", files.length);

    MultipartBodyBuilder builder = new MultipartBodyBuilder();

    Arrays.stream(files).forEach(file -> {
      LOGGER.info("파일 정보: 이름={}, 크기={}, 타입={}",
          file.getOriginalFilename(), file.getSize(), file.getContentType());

      String filename = file.getOriginalFilename();
      if (filename == null || filename.isEmpty()) {
        filename = "file";
      }

      builder.part("files", file.getResource()).filename(filename);
    });

    try {
      String[] response = webClient.post()
          .uri("/images")
          .contentType(MediaType.MULTIPART_FORM_DATA)
          .body(BodyInserters.fromMultipartData(builder.build()))
          .retrieve()
          .bodyToMono(String[].class)
          .block();

      LOGGER.info("NestJS 서버 응답: {}", Arrays.toString(response));
      return Arrays.asList(response != null ? response : new String[0]);

    } catch (Exception e) {
      LOGGER.error("NestJS 서버로 파일 업로드 실패: {}", e.getMessage(), e);
      throw new ApiException(ErrorCode.SAVE_IMAGE_FAILED);
    }
  }

  @Override
  public List<String> createImages(UUID entityId, List<String> images, EntityType entityType) {

    ImageRequest newRequest = ImageRequest.builder()
        .id(entityId)
        .images(images)
        .existingImages(Arrays.asList())
        .entity(entityType.name().toLowerCase())
        .build();

    List<String> response = webClient.post()
        .uri("/planova/create")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newRequest)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<String>>() {
        })
        .block();

    if (response == null) {
      throw new ApiException(ErrorCode.SAVE_IMAGE_FAILED);
    }
    response.forEach(url -> {
      Image image = Image.builder()
          .entityId(entityId)
          .entityType(entityType)
          .url(url)
          .build();
      imageRepository.save(image);
    });
    return response;
  }

  @Override
  public List<String> updateImages(UUID entityId, List<String> images, List<String> existingImages,
      EntityType entityType) {
    LOGGER.info("=== 이미지 업데이트 시작 ===");
    LOGGER.info("Entity ID: {}, Type: {}", entityId, entityType);
    LOGGER.info("새 이미지 개수: {}, 기존 이미지 개수: {}", images.size(), existingImages.size());

    ImageRequest newRequest = ImageRequest.builder()
        .id(entityId)
        .images(images)
        .existingImages(existingImages)
        .entity(entityType.name().toLowerCase())
        .build();

    try {
      List<String> response = webClient.put()
          .uri("/planova/update")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(newRequest)
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<List<String>>() {
          })
          .block();

      if (response == null) {
        throw new ApiException(ErrorCode.SAVE_IMAGE_FAILED);
      }

      LOGGER.info("NestJS 서버 응답: {}", response);

      // 기존 이미지 데이터 삭제
      List<Image> existingImageEntities = imageRepository.findByEntityIdAndEntityType(entityId, entityType);
      if (!existingImageEntities.isEmpty()) {
        imageRepository.deleteAll(existingImageEntities);
        LOGGER.info("기존 이미지 {} 개 삭제 완료", existingImageEntities.size());
      }

      // 새로운 이미지 데이터 저장
      response.forEach(url -> {
        Image image = Image.builder()
            .entityId(entityId)
            .entityType(entityType)
            .url(url)
            .build();
        imageRepository.save(image);
      });

      LOGGER.info("새 이미지 {} 개 저장 완료", response.size());
      return response;

    } catch (Exception e) {
      LOGGER.error("이미지 업데이트 실패: {}", e.getMessage(), e);
      throw new ApiException(ErrorCode.SAVE_IMAGE_FAILED);
    }
  }

  @Override
  public void deleteImages(UUID entityId, String serviceName, EntityType entityType) {
    ImageRequest newRequest = ImageRequest.builder()
        .id(entityId)
        .serviceName(serviceName)
        .entity(entityType.name().toLowerCase())
        .build();

    try {
      webClient.method(HttpMethod.DELETE)
          .uri("/planova/delete")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(newRequest)
          .retrieve()
          .bodyToMono(String.class)
          .block();

      List<Image> existingImageEntities = imageRepository.findByEntityIdAndEntityType(entityId, entityType);
      if (!existingImageEntities.isEmpty()) {
        imageRepository.deleteAll(existingImageEntities);
        LOGGER.info("기존 이미지 {} 개 삭제 완료", existingImageEntities.size());
      }

    } catch (Exception e) {
      LOGGER.error("이미지 삭제 실패: {}", e.getMessage(), e);
      throw new ApiException(ErrorCode.SAVE_IMAGE_FAILED);
    }
  }

  @Override
  public void createUserImage(UUID entityId, String image, EntityType entityType) {
    Image imageEntity = Image.builder()
        .entityId(entityId)
        .entityType(entityType)
        .url(image)
        .build();
    imageRepository.save(imageEntity);
  }
}
