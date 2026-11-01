package com.rental.rental_service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockMultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental.model.Booking;
import com.rental.model.Property;
import com.rental.repository.InquiryRepository;
import com.rental.repository.PropertyRepository;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:rental-test;MODE=MySQL;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
class RentalServiceApplicationTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private InquiryRepository inquiryRepository;

        @Autowired
        private PropertyRepository propertyRepository;

        @Test
        void shouldCreateUpdateAndDeleteProperty() throws Exception {
                Property property = new Property();
                property.setLandlordId(1L);
                property.setTitle("Test Villa");
                property.setDescription("Sample description");
                property.setType("Apartment");
                property.setRent(java.math.BigDecimal.valueOf(35000));
                property.setDeposit(java.math.BigDecimal.valueOf(70000));
                property.setAddress("12 Main St");
                property.setCity("Pune");
                property.setState("MH");
                property.setPincode("411001");
                property.setBedrooms(2);
                property.setBathrooms(2);
                property.setAvailable(true);

                String createResponse = mockMvc.perform(post("/api/properties")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(property)))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                Long createdId = objectMapper.readTree(createResponse).get("id").asLong();

                mockMvc.perform(get("/api/properties/{id}", createdId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Test Villa"));

                property.setTitle("Updated Villa");
                mockMvc.perform(put("/api/properties/{id}", createdId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(property)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Updated Villa"));

                mockMvc.perform(delete("/api/properties/{id}", createdId))
                                .andExpect(status().isNoContent());

                mockMvc.perform(get("/api/properties/{id}", createdId))
                                .andExpect(status().isNotFound());
        }

        @Test
        void shouldSaveInquiryForAnExistingProperty() throws Exception {
                Long propertyId = propertyRepository.findAll().get(0).getId();
                String response = mockMvc.perform(post("/api/inquiries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(java.util.Map.of(
                                                "propertyId", propertyId,
                                                "name", "Alex Morgan",
                                                "email", "alex@example.com",
                                                "message", "Is this home available?"))))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.propertyId").value(propertyId))
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                Long inquiryId = objectMapper.readTree(response).get("id").asLong();
                assertTrue(inquiryRepository.findById(inquiryId).isPresent());
        }

        @Test
        void shouldPersistAndServeUploadedPropertyPhoto() throws Exception {
                Property property = new Property();
                property.setTitle("Photo Test Listing");
                property.setCity("Pune");
                property.setRent(java.math.BigDecimal.valueOf(25000));
                byte[] image = new byte[] { 1, 2, 3, 4 };

                MockMultipartFile propertyPart = new MockMultipartFile(
                                "property", "", MediaType.APPLICATION_JSON_VALUE,
                                objectMapper.writeValueAsBytes(property));
                MockMultipartFile photoPart = new MockMultipartFile(
                                "photos", "front.png", MediaType.IMAGE_PNG_VALUE, image);

                String response = mockMvc.perform(multipart("/api/properties")
                                .file(propertyPart)
                                .file(photoPart))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.photoIds.length()").value(1))
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                var created = objectMapper.readTree(response);
                Long propertyId = created.get("id").asLong();
                Long photoId = created.get("photoIds").get(0).asLong();

                mockMvc.perform(get("/api/properties/{id}", propertyId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.photoIds[0]").value(photoId));
                mockMvc.perform(get("/api/properties/{propertyId}/photos/{photoId}", propertyId, photoId))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                                .andExpect(content().bytes(image));
                mockMvc.perform(get("/api/properties/{propertyId}/photos", propertyId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(photoId))
                                .andExpect(jsonPath("$[0].content").doesNotExist());
                mockMvc.perform(delete("/api/properties/{propertyId}/photos/{photoId}", propertyId, photoId))
                                .andExpect(status().isNoContent());
        }

        @Test
        void shouldCreateABookingWithPaymentDetails() throws Exception {
                Property property = new Property();
                property.setTitle("Booking Test Listing");
                property.setCity("Pune");
                property.setRent(java.math.BigDecimal.valueOf(42000));
                property.setAvailable(true);
                String propertyResponse = mockMvc.perform(post("/api/properties")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(property)))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();
                Long propertyId = objectMapper.readTree(propertyResponse).get("id").asLong();

                Booking booking = new Booking();
                booking.setPropertyId(propertyId);
                booking.setTenantId(99L);
                booking.setMoveInDate(java.time.LocalDate.now().plusDays(7));
                booking.setNumberOfGuests(3);
                booking.setTotalAmount(java.math.BigDecimal.valueOf(42000));
                booking.setAdvancePaid(java.math.BigDecimal.valueOf(21000));
                booking.setPendingBalance(java.math.BigDecimal.valueOf(21000));
                booking.setPaymentStatus("PARTIAL");
                booking.setBookingStatus("REQUESTED");

                mockMvc.perform(post("/api/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(booking)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.paymentStatus").value("PARTIAL"))
                                .andExpect(jsonPath("$.pendingBalance").value(21000));
        }

        @Test
        void shouldRecordPaymentAndDeriveRemainingBalance() throws Exception {
                Property property = new Property();
                property.setTitle("Payment Test Listing");
                property.setCity("Pune");
                property.setRent(java.math.BigDecimal.valueOf(42000));
                String propertyResponse = mockMvc.perform(post("/api/properties")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(property)))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();
                Long propertyId = objectMapper.readTree(propertyResponse).get("id").asLong();

                String bookingResponse = mockMvc.perform(post("/api/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {"propertyId":%d,"tenantId":99,"moveInDate":"%s",
                                                "numberOfGuests":2,"totalAmount":42000,"advancePaid":10000}
                                                """.formatted(propertyId, java.time.LocalDate.now().plusDays(3))))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.pendingBalance").value(32000))
                                .andReturn()
                                .getResponse()
                                .getContentAsString();
                Long bookingId = objectMapper.readTree(bookingResponse).get("id").asLong();

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch(
                                "/api/bookings/{id}/payment", bookingId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"amount\":12000}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.advancePaid").value(22000))
                                .andExpect(jsonPath("$.pendingBalance").value(20000))
                                .andExpect(jsonPath("$.paymentStatus").value("PARTIAL"));

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch(
                                "/api/bookings/{id}/payment", bookingId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"amount\":21000}"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void shouldListAndDeleteInquiriesByProperty() throws Exception {
                Long propertyId = propertyRepository.findAll().get(0).getId();
                String inquiryResponse = mockMvc.perform(post("/api/inquiries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(java.util.Map.of(
                                                "propertyId", propertyId,
                                                "name", "Taylor",
                                                "email", "taylor@example.com",
                                                "message", "Interested"))))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();
                Long inquiryId = objectMapper.readTree(inquiryResponse).get("id").asLong();

                mockMvc.perform(get("/api/inquiries/property/{propertyId}", propertyId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(inquiryId));
                mockMvc.perform(delete("/api/inquiries/{id}", inquiryId))
                                .andExpect(status().isNoContent());
        }
}
