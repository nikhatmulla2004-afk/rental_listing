package com.rental.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.rental.model.Property;
import com.rental.model.PropertyPhoto;
import com.rental.repository.BookingRepository;
import com.rental.repository.InquiryRepository;
import com.rental.repository.PropertyPhotoRepository;
import com.rental.repository.PropertyRepository;

@Service
public class PropertyService {
    private static final int MAX_PHOTOS = 5;
    private static final long MAX_PHOTO_SIZE = 10L * 1024 * 1024;
    private final PropertyRepository propertyRepository;
    private final PropertyPhotoRepository propertyPhotoRepository;
    private final BookingRepository bookingRepository;
    private final InquiryRepository inquiryRepository;

    public PropertyService(
            PropertyRepository propertyRepository,
            PropertyPhotoRepository propertyPhotoRepository,
            BookingRepository bookingRepository,
            InquiryRepository inquiryRepository) {
        this.propertyRepository = propertyRepository;
        this.propertyPhotoRepository = propertyPhotoRepository;
        this.bookingRepository = bookingRepository;
        this.inquiryRepository = inquiryRepository;
    }

    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    public Optional<Property> findById(@NonNull Long id) {
        return propertyRepository.findById(id);
    }

    @Transactional
    public Property create(@NonNull Property property) {
        return propertyRepository.save(property);
    }

    @Transactional
    public Property createWithPhotos(@NonNull Property property, List<MultipartFile> photos) {
        validateNewPhotos(photos);
        Property savedProperty = propertyRepository.save(property);
        savePhotos(savedProperty, photos);
        return savedProperty;
    }

    @Transactional
    public Property uploadPhotos(@NonNull Long propertyId, List<MultipartFile> photos) {
        validateNewPhotos(photos);
        Property property = requireProperty(propertyId);
        if (propertyPhotoRepository.countByProperty_Id(propertyId) + photos.size() > MAX_PHOTOS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A listing can have at most five photos");
        }
        savePhotos(property, photos);
        return property;
    }

    @Transactional
    public Property update(@NonNull Long id, @NonNull Property details) {
        Property existing = requireProperty(id);
        existing.setLandlordId(details.getLandlordId());
        existing.setTitle(details.getTitle());
        existing.setDescription(details.getDescription());
        existing.setType(details.getType());
        existing.setFurnishingStatus(details.getFurnishingStatus());
        existing.setAreaSqft(details.getAreaSqft());
        existing.setRent(details.getRent());
        existing.setDeposit(details.getDeposit());
        existing.setAddress(details.getAddress());
        existing.setCity(details.getCity());
        existing.setState(details.getState());
        existing.setPincode(details.getPincode());
        existing.setBedrooms(details.getBedrooms());
        existing.setBathrooms(details.getBathrooms());
        existing.setAvailable(details.getAvailable());
        existing.setAmenities(details.getAmenities());
        return propertyRepository.save(existing);
    }

    @Transactional
    public void delete(@NonNull Long id) {
        Property property = requireProperty(id);
        if (bookingRepository.existsByProperty_Id(id) || inquiryRepository.existsByProperty_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A property with bookings or inquiries cannot be deleted");
        }
        propertyPhotoRepository.deleteAllByProperty_Id(id);
        propertyRepository.delete(property);
    }

    public List<Property> search(
            String city,
            String type,
            BigDecimal minRent,
            BigDecimal maxRent,
            Integer bedrooms,
            Integer bathrooms,
            String amenity) {
        if (minRent != null && maxRent != null && minRent.compareTo(maxRent) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum rent cannot exceed maximum rent");
        }
        return propertyRepository.findAll().stream()
                .filter(property -> city == null || city.isBlank()
                        || property.getCity() != null && property.getCity().equalsIgnoreCase(city.trim()))
                .filter(property -> type == null || type.isBlank()
                        || property.getType() != null && property.getType().equalsIgnoreCase(type.trim()))
                .filter(property -> minRent == null
                        || property.getRent() != null && property.getRent().compareTo(minRent) >= 0)
                .filter(property -> maxRent == null
                        || property.getRent() != null && property.getRent().compareTo(maxRent) <= 0)
                .filter(property -> bedrooms == null
                        || property.getBedrooms() != null && property.getBedrooms() >= bedrooms)
                .filter(property -> bathrooms == null
                        || property.getBathrooms() != null && property.getBathrooms() >= bathrooms)
                .filter(property -> amenity == null || amenity.isBlank()
                        || property.getAmenities().stream().anyMatch(value -> value.equalsIgnoreCase(amenity.trim())))
                .toList();
    }

    public List<PropertyPhoto> findPhotos(@NonNull Long propertyId) {
        requireProperty(propertyId);
        return propertyPhotoRepository.findAllByProperty_IdOrderById(propertyId);
    }

    @Transactional
    public void deletePhoto(@NonNull Long propertyId, @NonNull Long photoId) {
        if (!propertyRepository.existsById(propertyId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found");
        }
        if (propertyPhotoRepository.findByIdAndProperty_Id(photoId, propertyId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found");
        }
        propertyPhotoRepository.deleteByIdAndProperty_Id(photoId, propertyId);
    }

    private void savePhotos(Property property, List<MultipartFile> photos) {
        for (MultipartFile photo : photos) {
            String contentType = photo.getContentType();
            try {
                PropertyPhoto savedPhoto = propertyPhotoRepository.save(
                        new PropertyPhoto(property, contentType, photo.getBytes()));
                property.addPhoto(savedPhoto);
            } catch (IOException exception) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not read an uploaded photo", exception);
            }
        }
    }

    private void validateNewPhotos(List<MultipartFile> photos) {
        if (photos == null || photos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one photo is required");
        }
        if (photos.size() > MAX_PHOTOS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A listing can have at most five photos");
        }
        for (MultipartFile photo : photos) {
            String contentType = photo.getContentType();
            if (photo.isEmpty() || photo.getSize() > MAX_PHOTO_SIZE
                    || !(MediaType.IMAGE_JPEG_VALUE.equals(contentType)
                            || MediaType.IMAGE_PNG_VALUE.equals(contentType))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Photos must be non-empty JPG or PNG files up to 10 MB");
            }
        }
    }

    private Property requireProperty(@NonNull Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));
    }
}
