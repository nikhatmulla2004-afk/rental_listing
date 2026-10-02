
package com.rental.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.model.Property;
import com.rental.repository.PropertyRepository;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping
    public List<Property> getAll() {
        return propertyRepository.findAll();
    }

    @PostMapping
    public Property create(@RequestBody Property property) {
        return propertyRepository.save(property);
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

        propertyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
