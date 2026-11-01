package com.rental.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "property_photos")
public class PropertyPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    @JsonIgnore
    private Property property;

    @NonNull
    @Column(nullable = false)
    private String contentType;

    @Lob
    @JsonIgnore
    private byte[] content;

    protected PropertyPhoto() {
    }

    public PropertyPhoto(Property property, String contentType, byte[] content) {
        this.property = property;
        this.contentType = contentType;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    @JsonProperty
    public Long getPropertyId() {
        return property == null ? null : property.getId();
    }

    @JsonIgnore
    public Property getProperty() {
        return property;
    }

    @NonNull
    public String getContentType() {
        return contentType;
    }

    public byte[] getContent() {
        return content;
    }
}