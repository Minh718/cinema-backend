package com.movie.roomservice.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., CGV Aeon Mall Tân Phú
    private String brand; // e.g., CGV, Lotte, BHD
    private String description; // Optional details about this cinema

    private String city; // e.g., Hồ Chí Minh
    private String district; // e.g., Tân Phú
    private String address; // Full street address

    private String phoneNumber; // Hotline or local number
    private String email; // Optional

    private String imageUrl; // Optional: thumbnail/logo for UI

    private Boolean isActive = true; // Logical delete support
    // 🗺️ Google Maps Integration
    private String mapUrl; // Optional Google Maps URL
    private Double latitude; // e.g., 10.762622
    private Double longitude; // e.g., 106.660172
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
