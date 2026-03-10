package com.taskmanager.backend.dto;

import org.springframework.web.multipart.MultipartFile;


public class UpdateProfilePictureDto {
    private byte[] data;
    private String contentType;

    public UpdateProfilePictureDto(MultipartFile file) throws java.io.IOException {
        this.data = file.getBytes();
        this.contentType = file.getContentType();
    }

    public byte[] getData() { return data; }
    public String getContentType() { return contentType; }
}