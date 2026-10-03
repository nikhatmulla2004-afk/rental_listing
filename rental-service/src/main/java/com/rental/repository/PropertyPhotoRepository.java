package com.rental.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental.model.PropertyPhoto;

public interface PropertyPhotoRepository extends JpaRepository<PropertyPhoto, Long> {
    Optional<PropertyPhoto> findByIdAndPropertyId(Long id, Long propertyId);

    void deleteAllByPropertyId(Long propertyId);
}