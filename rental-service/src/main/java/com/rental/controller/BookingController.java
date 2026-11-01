package com.rental.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.dto.BookingStatusRequest;
import com.rental.dto.PaymentRequest;
import com.rental.model.Booking;
import com.rental.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> getAll() {
        return bookingService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        return bookingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.create(booking));
    }

    @GetMapping("/property/{propertyId}")
    public List<Booking> getBookingsByProperty(@PathVariable Long propertyId) {
        return bookingService.findByProperty(propertyId);
    }

    @GetMapping("/tenant/{tenantId}")
    public List<Booking> getBookingsByTenant(@PathVariable Long tenantId) {
        return bookingService.findByTenant(tenantId);
    }

    @PatchMapping("/{id}/payment")
    public Booking recordPayment(@PathVariable Long id, @RequestBody PaymentRequest request) {
        BigDecimal amount = request == null ? null : request.amount();
        return bookingService.recordPayment(id, amount);
    }

    @PatchMapping("/{id}/status")
    public Booking updateStatus(@PathVariable Long id, @RequestBody BookingStatusRequest request) {
        return bookingService.updateStatus(id, request == null ? null : request.status());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
