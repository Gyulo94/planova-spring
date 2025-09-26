package com.planova.server.image.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.planova.server.image.entity.EntityType;
import com.planova.server.image.entity.Image;

public interface ImageService {

  List<Image> findImagesByEntityId(UUID entityId, EntityType entityType);

  List<String> uploadImages(MultipartFile[] files);

  List<String> createImages(UUID entityId, List<String> images, EntityType entityType);

  List<String> updateImages(UUID entityId, List<String> images, List<String> existingImages, EntityType entityType);

  void deleteImages(UUID entityId, String serviceName, EntityType entityType);

}
