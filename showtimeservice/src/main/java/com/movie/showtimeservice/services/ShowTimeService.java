package com.movie.showtimeservice.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.movie.showtimeservice.dtos.requests.ShowTimeGroupCreateReq;
import com.movie.showtimeservice.dtos.responses.ShowTimeDetailRes;
import com.movie.showtimeservice.dtos.responses.ShowTimeGroupRes;
import com.movie.showtimeservice.dtos.responses.ShowTimeRes;
import com.movie.showtimeservice.dtos.responses.ShowTimesOfMovieAndDateRes;
import com.movie.showtimeservice.entities.ShowTime;
import com.movie.showtimeservice.entities.ShowTimeGroup;
import com.movie.showtimeservice.enums.TypeShowTime;
import com.movie.showtimeservice.exceptions.CustomException;
import com.movie.showtimeservice.exceptions.ErrorCode;
import com.movie.showtimeservice.mappers.ShowTimeGroupMapper;
import com.movie.showtimeservice.mappers.ShowTimeMapper;
import com.movie.showtimeservice.repositories.ShowTimeGroupRepository;
import com.movie.showtimeservice.repositories.ShowTimeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowTimeService {

    private final ShowTimeRepository showTimeRepository;
    private final ShowTimeGroupRepository showTimeGroupRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public ShowTimeRes addShowTimeToGroupShowTime(Long showTimeGroupId) {
        int bufferMinutes = 15;
        LocalTime opening = LocalTime.of(8, 0);
        LocalTime closing = LocalTime.of(23, 0);

        ShowTimeGroup showTimeGroup = showTimeGroupRepository.findById(showTimeGroupId)
                .orElseThrow(() -> new CustomException(ErrorCode.SHOWTIMEGROUP_NOT_FOUND));

        String redisKey = "lastEndTime:" + showTimeGroup.getRoomId() + ":" + showTimeGroup.getDate().toString();

        String endTimeStr = redisTemplate.opsForValue().get(redisKey);

        LocalTime currentStart = endTimeStr != null
                ? roundUpToQuarter(LocalTime.parse(endTimeStr)).plusMinutes(bufferMinutes)
                : opening;

        LocalTime currentEnd = currentStart.plusMinutes(showTimeGroup.getDuration());

        if (currentEnd.isAfter(closing)) {
            redisTemplate.delete(redisKey);
            throw new RuntimeException("No available time slot");
        }

        ShowTime newShowTime = ShowTime.builder().startTime(currentEnd).endTime(currentEnd).build();
        newShowTime.setShowTimeGroup(showTimeGroup);
        cacheLastEndTime(redisKey, newShowTime.getEndTime());

        return ShowTimeMapper.INSTANCE.toShowTimeRes(showTimeRepository.save(newShowTime));
    }

    private LocalTime roundUpToQuarter(LocalTime time) {
        int minute = time.getMinute();
        int rounded = ((minute + 14) / 15) * 15;
        return time.withMinute(0).plusMinutes(rounded).withSecond(0).withNano(0);
    }

    private void cacheLastEndTime(String redisKey, LocalTime endTime) {
        redisTemplate.opsForValue().set(redisKey, endTime.toString());
    }

    public ShowTimeGroup createGroupShowTime(ShowTimeGroupCreateReq request) {
        return showTimeGroupRepository.save(ShowTimeGroupMapper.INSTANCE.toShowTimeGroup(request));
    }

    public List<LocalDate> getFutureShowDatesByMovieIdAndCinemaId(Long movieId, Long cinemaId) {
        return showTimeGroupRepository.findDistinctFutureDatesByMovieIdAndCinemaId(movieId, cinemaId);
    }

    public ShowTimesOfMovieAndDateRes getShowTimesByMovieIdAndCinemaIdAndDate(Long movieId, Long cinemaId,
            LocalDate date) {
        ShowTimeGroup showTimeGroupDub = showTimeGroupRepository.findByMovieIdAndCinemaIdAndDateAndStatusAndType(
                movieId, cinemaId, date,
                "ACTIVE", TypeShowTime.DUBBED);

        ShowTimeGroup showTimeGroupSub = showTimeGroupRepository.findByMovieIdAndCinemaIdAndDateAndStatusAndType(
                movieId, cinemaId, date,
                "ACTIVE", TypeShowTime.SUBTITLE);
        List<ShowTimeRes> dubShows = ShowTimeMapper.INSTANCE.toShowTimeRes(showTimeGroupDub.getShowTimes());
        List<ShowTimeRes> subShows = ShowTimeMapper.INSTANCE.toShowTimeRes(showTimeGroupSub.getShowTimes());
        return new ShowTimesOfMovieAndDateRes(dubShows, subShows);
    }

    public List<Long> getNowShowingMovieIdsByCinemaId(Long cinemaId) {

        return showTimeGroupRepository.findNowShowingMovieIdsByCinemaId(cinemaId, "ACTIVE");
    }

    public ShowTimeDetailRes getBookableDetailShowTime(Long showTimeId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        ShowTimeDetailRes showtime = showTimeGroupRepository.findBookableDetailShowTimeById(showTimeId, today, now)
                .orElseThrow(() -> new CustomException(ErrorCode.SHOWTIME_NOT_FOUND));
        return showtime;
    }

    public List<ShowTimeGroupRes> getShowTimeGroupsInDate(LocalDate date, TypeShowTime type) {
        List<ShowTimeGroup> showTimeGroups = showTimeGroupRepository.findShowTimeGroupsByTypeInDate(date, type);

        return ShowTimeGroupMapper.INSTANCE.toShowTimeGroupRes(showTimeGroups);

    }

    public List<ShowTimeGroupRes> getShowTimeGroupsByMovieIdInDate(LocalDate date, Long movieId) {
        List<ShowTimeGroup> showTimeGroups = showTimeGroupRepository.findShowTimeGroupsByMovieIdInDate(date, movieId);

        return ShowTimeGroupMapper.INSTANCE.toShowTimeGroupRes(showTimeGroups);

    }
}