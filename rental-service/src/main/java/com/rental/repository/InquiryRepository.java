package com.rental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental.model.Inquiry;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByProperty_IdOrderByCreatedAtDesc(Long propertyId);

    boolean existsByProperty_Id(Long propertyId);
}