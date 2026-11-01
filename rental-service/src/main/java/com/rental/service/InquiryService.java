package com.rental.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.rental.dto.InquiryRequest;
import com.rental.model.Inquiry;
import com.rental.repository.InquiryRepository;
import com.rental.repository.PropertyRepository;

@Service
public class InquiryService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private final InquiryRepository inquiryRepository;
    private final PropertyRepository propertyRepository;

    public InquiryService(InquiryRepository inquiryRepository, PropertyRepository propertyRepository) {
        this.inquiryRepository = inquiryRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<Inquiry> findAll() {
        return inquiryRepository.findAll();
    }

    public Optional<Inquiry> findById(@NonNull Long id) {
        return inquiryRepository.findById(id);
    }

    public List<Inquiry> findByProperty(@NonNull Long propertyId) {
        if (!propertyRepository.existsById(propertyId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found");
        }
        return inquiryRepository.findByProperty_IdOrderByCreatedAtDesc(propertyId);
    }

    @Transactional
    public Inquiry create(InquiryRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inquiry details are missing or invalid");
        }
        Long propertyId = request.propertyId();
        if (propertyId == null || request.name() == null || request.name().isBlank() || request.email() == null
                || !EMAIL_PATTERN.matcher(request.email().trim()).matches()
                || request.message() != null && request.message().length() > 2000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inquiry details are missing or invalid");
        }
        var property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));

        Inquiry inquiry = new Inquiry();
        inquiry.setProperty(property);
        inquiry.setName(request.name().trim());
        inquiry.setEmail(request.email().trim());
        inquiry.setMessage(request.message());
        return inquiryRepository.save(inquiry);
    }

    @Transactional
    public void delete(@NonNull Long id) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inquiry not found"));
        inquiryRepository.delete(inquiry);
    }
}
