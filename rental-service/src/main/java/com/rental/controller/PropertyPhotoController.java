package com.rental.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rental.model.PropertyPhoto;
import com.rental.repository.PropertyPhotoRepository;
import com.rental.service.PropertyService;

@RestController
@RequestMapping("/api/properties/{propertyId}/photos")
@CrossOrigin(origins = "*")
public class PropertyPhotoController {
    private final PropertyPhotoRepository propertyPhotoRepository;
    private final PropertyService propertyService;

    public PropertyPhotoController(
            PropertyPhotoRepository propertyPhotoRepository,
            PropertyService propertyService) {
        this.propertyPhotoRepository = propertyPhotoRepository;
        this.propertyService = propertyService;
    }

    @GetMapping
    public List<PropertyPhoto> getPhotos(@PathVariable Long propertyId) {
        return propertyService.findPhotos(propertyId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<Long>> addPhotos(
            @PathVariable Long propertyId,
            @RequestPart("photos") List<MultipartFile> photos) {
        var property = propertyService.uploadPhotos(propertyId, photos);
        return ResponseEntity.status(HttpStatus.CREATED).body(property.getPhotoIds());
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long propertyId, @PathVariable Long photoId) {
        return propertyPhotoRepository.findByIdAndProperty_Id(photoId, propertyId)
                .map(this::toResponse)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long propertyId, @PathVariable Long photoId) {
        propertyService.deletePhoto(propertyId, photoId);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<byte[]> toResponse(PropertyPhoto photo) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(photo.getContentType()))
                .body(photo.getContent());
    }
}
