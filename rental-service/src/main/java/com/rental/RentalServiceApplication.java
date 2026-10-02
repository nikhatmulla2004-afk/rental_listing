package com.rental;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.rental.model.Property;
import com.rental.repository.PropertyRepository;

@SpringBootApplication
public class RentalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedDemoListings(PropertyRepository propertyRepository) {
        return args -> {
            if (propertyRepository.count() > 0) {
                return;
            }

            propertyRepository.saveAll(List.of(
                    createProperty("The Courtyard House", "Pune", "MH", "Apartment", 28500, 2, 2,
                            "A quiet, sun-washed home tucked into one of the city's most walkable neighborhoods."),
                    createProperty("A little house in the trees", "Bengaluru", "KA", "House", 42000, 3, 2,
                            "A leafy hideaway with generous rooms and a slow morning kind of light."),
                    createProperty("The blue door studio", "Mumbai", "MH", "Studio", 31000, 1, 1,
                            "Compact, calm, and close to all the good things in Bandra."),
                    createProperty("Sunset over the old city", "Jaipur", "RJ", "Villa", 56000, 3, 3,
                            "An airy villa where afternoon light moves across terracotta floors.")));
        };
    }

    private static Property createProperty(String title, String city, String state, String type,
            int rent, int bedrooms, int bathrooms, String description) {
        Property property = new Property();
        property.setTitle(title);
        property.setCity(city);
        property.setState(state);
        property.setType(type);
        property.setRent(BigDecimal.valueOf(rent));
        property.setDeposit(BigDecimal.valueOf(rent * 2L));
        property.setBedrooms(bedrooms);
        property.setBathrooms(bathrooms);
        property.setDescription(description);
        property.setAddress(city);
        property.setAvailable(true);
        return property;
    }
}
