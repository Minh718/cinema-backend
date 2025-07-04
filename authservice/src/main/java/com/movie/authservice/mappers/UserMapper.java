package com.movie.authservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.movie.authservice.dtos.response.UserInfoToken;
import com.movie.authservice.entities.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(ignore = true, target = "accessToken")
    @Mapping(ignore = true, target = "refreshToken")
    UserInfoToken toUserInfoToken(User user);

}