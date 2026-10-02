package com.rental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental.model.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}