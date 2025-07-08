package com.movie.movieservice.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetadataDTO {
    private long totalItems;
    private int totalPages;
    private int currentPage;
    private int itemsPerPage;

}
