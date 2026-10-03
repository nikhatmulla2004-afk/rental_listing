package com.rental.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.model.PropertyPhoto;
import com.rental.repository.PropertyPhotoRepository;

@RestController
@RequestMapping("/api/properties/{propertyId}/photos")
@CrossOrigin(origins = "*")
public class PropertyPhotoController {
    private final PropertyPhotoRepository propertyPhotoRepository;

    public PropertyPhotoController(PropertyPhotoRepository propertyPhotoRepository) {
        this.propertyPhotoRepository = propertyPhotoRepository;
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long propertyId, @PathVariable Long photoId) {
        return propertyPhotoRepository.findByIdAndPropertyId(photoId, propertyId)
                .map(this::toResponse)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<byte[]> toResponse(PropertyPhoto photo) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(photo.getContentType()))
                .body(photo.getContent());
    }
}