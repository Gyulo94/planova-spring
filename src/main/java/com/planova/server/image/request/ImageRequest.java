package com.planova.server.image.request;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "IMAGE_REQ_01 : 이미지 요청 DTO")
public class ImageRequest {

    @Schema(description = "ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "이미지 목록", example = "[\"https://gyubuntu.duckdns.org/uploads/planova/images/image1.jpg\", \"https://gyubuntu.duckdns.org/uploads/planova/images/image2.jpg\"]")
    private List<String> images;

    @Schema(description = "서비스 이름", example = "planova")
    private String serviceName;

    @Schema(description = "기존 이미지 경로 목록 (이미지 첫등록시 빈배열)", example = "[\"https://gyubuntu.duckdns.org/uploads/planova/images/oldImage1.jpg\", \"https://gyubuntu.duckdns.org/uploads/planova/images/oldImage2.jpg\"]")
    private List<String> existingImages;
    private String entity;
}
