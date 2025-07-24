package com.movie.cinemaservice.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.movie.cinemaservice.dtos.requests.CinemaCreateReq;
import com.movie.cinemaservice.dtos.responses.ChatBoxGroupRes;
import com.movie.cinemaservice.dtos.responses.CinemaRes;
import com.movie.cinemaservice.entities.Cinema;
import com.movie.cinemaservice.exceptions.CustomException;
import com.movie.cinemaservice.exceptions.ErrorCode;
import com.movie.cinemaservice.mappers.CinemaMapper;
import com.movie.cinemaservice.repositories.CinemaRepository;
import com.movie.cinemaservice.repositories.httpClients.MessagingClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;
    private final StorageService storageService;
    private final MessagingClient messagingClient;

    public List<CinemaRes> getAllCinemas() {

        List<Cinema> cinemas = cinemaRepository.findAllByStatus(true);
        return CinemaMapper.INSTANCE.toCinemaResList(cinemas);
    }

    public Cinema createCinema(CinemaCreateReq request, MultipartFile image) {
        String imageUrl = storageService.saveImage(image);

        Cinema cinema = CinemaMapper.INSTANCE.toCinema(request);
        cinema.setImageUrl(imageUrl);

        ChatBoxGroupRes chatBoxGroup = messagingClient.createChatBoxGroup(cinema.getName()).getResult();
        cinema.setChatBoxId(chatBoxGroup.getId());

        return cinemaRepository.save(cinema);
    }

    public CinemaRes getDetailCinema(Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new CustomException(ErrorCode.CINEMA_NOT_FOUND));
        return CinemaMapper.INSTANCE.toCinemaRes(cinema);
    }
}