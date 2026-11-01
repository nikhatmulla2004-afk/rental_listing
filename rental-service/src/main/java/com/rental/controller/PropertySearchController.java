package com.rental.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rental.model.Property;
import com.rental.service.PropertyService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PropertySearchController {
    private final PropertyService propertyService;

    public PropertySearchController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/properties/search")
    public ResponseEntity<List<Property>> searchProperties(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String minRent,
            @RequestParam(required = false) String maxRent,
            @RequestParam(required = false) String bedrooms,
            @RequestParam(required = false) String bathrooms,
            @RequestParam(required = false) String amenities) {
        return ResponseEntity.ok(propertyService.search(
                city,
                type,
                parseDecimal("minRent", minRent),
                parseDecimal("maxRent", maxRent),
                parseInteger("bedrooms", bedrooms),
                parseInteger("bathrooms", bathrooms),
                amenities));
    }

    private BigDecimal parseDecimal(String name, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            BigDecimal amount = new BigDecimal(value);
            if (amount.signum() < 0) {
                throw new NumberFormatException();
            }
            return amount;
        } catch (NumberFormatException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, name + " must be a non-negative number");
        }
    }

    private Integer parseInteger(String name, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            int amount = Integer.parseInt(value);
            if (amount < 0) {
                throw new NumberFormatException();
            }
            return amount;
        } catch (NumberFormatException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, name + " must be a non-negative integer");
        }
    }
}
