package com.movie.showtimeservice.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.movie.showtimeservice.enums.TypeShowTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_showtimegroup", uniqueConstraints = @UniqueConstraint(columnNames = { "date", "movieId", "roomId",
        "type" }))
public class ShowTimeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private Long movieId;
    private Long roomId;
    private Long cinemaId;
    @Enumerated(EnumType.STRING)
    private TypeShowTime type;

    @Builder.Default
    private String status = "ACTIVE";
    private String language;
    private Double basePrice;
    private Long duration;

    @OneToMany(mappedBy = "showTimeGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShowTime> showTimes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
