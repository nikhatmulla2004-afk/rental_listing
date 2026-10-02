package com.rental.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.dto.InquiryRequest;
import com.rental.model.Inquiry;
import com.rental.repository.InquiryRepository;
import com.rental.repository.PropertyRepository;

@RestController
@RequestMapping("/api/inquiries")
@CrossOrigin(origins = "*")
public class InquiryController {
    private final InquiryRepository inquiryRepository;
    private final PropertyRepository propertyRepository;

    public InquiryController(InquiryRepository inquiryRepository, PropertyRepository propertyRepository) {
        this.inquiryRepository = inquiryRepository;
        this.propertyRepository = propertyRepository;
    }

    @PostMapping
    public ResponseEntity<Inquiry> create(@RequestBody InquiryRequest request) {
        if (request.propertyId() == null || request.name() == null || request.name().isBlank()
                || request.email() == null || request.email().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (!propertyRepository.existsById(request.propertyId())) {
            return ResponseEntity.notFound().build();
        }

        Inquiry inquiry = new Inquiry();
        inquiry.setPropertyId(request.propertyId());
        inquiry.setName(request.name().trim());
        inquiry.setEmail(request.email().trim());
        inquiry.setMessage(request.message());
        return ResponseEntity.status(HttpStatus.CREATED).body(inquiryRepository.save(inquiry));
    }
}