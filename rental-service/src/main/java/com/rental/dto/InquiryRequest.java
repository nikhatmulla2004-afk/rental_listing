package com.rental.dto;

public record InquiryRequest(Long propertyId, String name, String email, String message) {
}