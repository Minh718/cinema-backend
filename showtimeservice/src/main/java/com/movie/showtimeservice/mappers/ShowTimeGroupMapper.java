package com.movie.showtimeservice.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.movie.showtimeservice.dtos.requests.ShowTimeGroupCreateReq;
import com.movie.showtimeservice.dtos.responses.ShowTimeGroupRes;
import com.movie.showtimeservice.entities.ShowTimeGroup;

@Mapper
public interface ShowTimeGroupMapper {
    ShowTimeGroupMapper INSTANCE = Mappers.getMapper(ShowTimeGroupMapper.class);

    ShowTimeGroup toShowTimeGroup(ShowTimeGroupCreateReq showTimeGroup);

    ShowTimeGroupRes toShowTimeGroupRes(ShowTimeGroup showTimeGroup);

    List<ShowTimeGroupRes> toShowTimeGroupRes(List<ShowTimeGroup> showTimeGroups);
}
