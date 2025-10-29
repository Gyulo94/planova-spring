package com.planova.server.image.response;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "IMAGE_RES_01 : 이미지 응답 DTO")
public class ImageResponse {

    @Schema(description = "ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "이미지 경로 목록", example = "[\"https://gyubuntu.duckdns.org/uploads/planova/images/image1.jpg\", \"https://gyubuntu.duckdns.org/uploads/planova/images/image2.jpg\"]")
    private List<String> images;

    @Schema(description = "기존 이미지 경로 목록 (이미지 첫등록시 빈배열)", example = "[\"https://gyubuntu.duckdns.org/uploads/planova/images/oldImage1.jpg\", \"https://gyubuntu.duckdns.org/uploads/planova/images/oldImage2.jpg\"]")
    private List<String> existingImages;

    @Schema(description = "엔터티 이름", example = "project")
    private String entity;
}
