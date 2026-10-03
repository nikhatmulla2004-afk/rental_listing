
package com.rental.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.rental.model.Property;
import com.rental.model.PropertyPhoto;
import com.rental.repository.PropertyPhotoRepository;
import com.rental.repository.PropertyRepository;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyController {
    private static final int MAX_PHOTOS = 5;
    private static final long MAX_PHOTO_SIZE = 10L * 1024 * 1024;

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private PropertyPhotoRepository propertyPhotoRepository;

    @GetMapping
    public List<Property> getAll() {
        return propertyRepository.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Property create(@RequestBody Property property) {
        return propertyRepository.save(property);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public Property createWithPhotos(
            @RequestPart("property") Property property,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos) {
        if (photos != null && photos.size() > MAX_PHOTOS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A listing can have at most five photos");
        }

        Property savedProperty = propertyRepository.save(property);
        if (photos == null) {
            return savedProperty;
        }

        for (MultipartFile photo : photos) {
            String contentType = photo.getContentType();
            if (photo.isEmpty() || photo.getSize() > MAX_PHOTO_SIZE
                    || !(MediaType.IMAGE_JPEG_VALUE.equals(contentType)
                            || MediaType.IMAGE_PNG_VALUE.equals(contentType))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Photos must be non-empty JPG or PNG files up to 10 MB");
            }

            try {
                PropertyPhoto savedPhoto = propertyPhotoRepository.save(
                        new PropertyPhoto(savedProperty.getId(), contentType, photo.getBytes()));
                savedProperty.getPhotoIds().add(savedPhoto.getId());
            } catch (IOException exception) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not read an uploaded photo",
                        exception);
            }
        }

        return propertyRepository.save(savedProperty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getById(@PathVariable Long id) {
        return propertyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> update(@PathVariable Long id, @RequestBody Property propertyDetails) {
        return propertyRepository.findById(id)
                .map(existingProperty -> {
                    existingProperty.setLandlordId(propertyDetails.getLandlordId());
                    existingProperty.setTitle(propertyDetails.getTitle());
                    existingProperty.setDescription(propertyDetails.getDescription());
                    existingProperty.setType(propertyDetails.getType());
                    existingProperty.setRent(propertyDetails.getRent());
                    existingProperty.setDeposit(propertyDetails.getDeposit());
                    existingProperty.setAddress(propertyDetails.getAddress());
                    existingProperty.setCity(propertyDetails.getCity());
                    existingProperty.setState(propertyDetails.getState());
                    existingProperty.setPincode(propertyDetails.getPincode());
                    existingProperty.setBedrooms(propertyDetails.getBedrooms());
                    existingProperty.setBathrooms(propertyDetails.getBathrooms());
                    existingProperty.setAvailable(propertyDetails.getAvailable());
                    return ResponseEntity.ok(propertyRepository.save(existingProperty));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!propertyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        propertyPhotoRepository.deleteAllByPropertyId(id);
        propertyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
