package com.movie.movieservice.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.movie.movieservice.enums.MovieStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_movie")
public class Movie {
    @Id
    private Long id;

    private String title;
    private String originalTitle;
    private String ageRating;
    private String overview;
    private Integer duration;
    private String director;
    private String language;
    private Set<Genre> genres;
    private MovieStatus status;
    private String thumbnailUrl;
    private LocalDate releaseDate;
    private String backdropPath;
    private String posterPath;
    private Double rating;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
