package com.rental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rental.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByProperty_Id(Long propertyId);

    List<Booking> findByTenantId(Long tenantId);

    boolean existsByProperty_Id(Long propertyId);
}
