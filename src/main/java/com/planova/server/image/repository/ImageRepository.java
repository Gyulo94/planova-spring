package com.planova.server.image.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.planova.server.image.entity.EntityType;
import com.planova.server.image.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
  List<Image> findByEntityIdAndEntityType(UUID entityId, EntityType entityType);
}
