package com.rental.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental.model.PropertyPhoto;

public interface PropertyPhotoRepository extends JpaRepository<PropertyPhoto, Long> {
    Optional<PropertyPhoto> findByIdAndProperty_Id(Long id, Long propertyId);

    List<PropertyPhoto> findAllByProperty_IdOrderById(Long propertyId);

    long countByProperty_Id(Long propertyId);

    void deleteAllByProperty_Id(Long propertyId);

    void deleteByIdAndProperty_Id(Long id, Long propertyId);
}