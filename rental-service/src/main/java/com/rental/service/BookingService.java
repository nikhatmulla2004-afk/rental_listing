package com.rental.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.rental.model.Booking;
import com.rental.repository.BookingRepository;
import com.rental.repository.PropertyRepository;

@Service
public class BookingService {
    private static final Set<String> BOOKING_STATUSES = Set.of("REQUESTED", "CONFIRMED", "CANCELLED", "COMPLETED");
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;

    public BookingService(BookingRepository bookingRepository, PropertyRepository propertyRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> findById(@NonNull Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> findByProperty(@NonNull Long propertyId) {
        requireProperty(propertyId);
        return bookingRepository.findByProperty_Id(propertyId);
    }

    public List<Booking> findByTenant(@NonNull Long tenantId) {
        if (tenantId == null || tenantId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tenant ID must be positive");
        }
        return bookingRepository.findByTenantId(tenantId);
    }

    @Transactional
    public Booking create(Booking booking) {
        validateBooking(booking);
        var property = requireProperty(booking.getPropertyId());
        if (!Boolean.TRUE.equals(property.getAvailable())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This property is not available");
        }
        if (booking.getMoveInDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Move-in date cannot be in the past");
        }

        booking.setProperty(property);
        booking.setBookingStatus("REQUESTED");
        updatePaymentFields(booking, booking.getAdvancePaid());
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking recordPayment(@NonNull Long id, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment amount must be greater than zero");
        }
        Booking booking = requireBooking(id);
        if (amount.compareTo(booking.getPendingBalance()) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment cannot exceed the pending balance");
        }
        updatePaymentFields(booking, booking.getAdvancePaid().add(amount));
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateStatus(@NonNull Long id, String status) {
        Booking booking = requireBooking(id);
        if (status == null || !BOOKING_STATUSES.contains(status.trim().toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported booking status");
        }
        booking.setBookingStatus(status.trim().toUpperCase());
        return bookingRepository.save(booking);
    }

    @Transactional
    public void delete(@NonNull Long id) {
        Booking booking = requireBooking(id);
        bookingRepository.delete(booking);
    }

    private void validateBooking(Booking booking) {
        if (booking == null || booking.getPropertyId() == null || booking.getTenantId() == null
                || booking.getTenantId() <= 0 || booking.getMoveInDate() == null
                || booking.getNumberOfGuests() == null || booking.getNumberOfGuests() <= 0
                || booking.getTotalAmount() == null || booking.getAdvancePaid() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Required booking details are missing or invalid");
        }
        if (booking.getTotalAmount().signum() <= 0 || booking.getAdvancePaid().signum() < 0
                || booking.getAdvancePaid().compareTo(booking.getTotalAmount()) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking amounts are invalid");
        }
    }

    private void updatePaymentFields(Booking booking, BigDecimal advancePaid) {
        BigDecimal pendingBalance = booking.getTotalAmount().subtract(advancePaid);
        booking.setAdvancePaid(advancePaid);
        booking.setPendingBalance(pendingBalance);
        booking.setPaymentStatus(pendingBalance.signum() == 0 ? "PAID" : advancePaid.signum() == 0 ? "PENDING" : "PARTIAL");
    }

    private com.rental.model.Property requireProperty(@NonNull Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));
    }

    private Booking requireBooking(@NonNull Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
    }
}
