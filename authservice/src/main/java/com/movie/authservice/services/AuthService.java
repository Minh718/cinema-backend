package com.movie.authservice.services;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.movie.authservice.constants.RoleUser;
import com.movie.authservice.dtos.request.GetTokenGoogleReq;
import com.movie.authservice.dtos.response.GetTokenGoogleRes;
import com.movie.authservice.dtos.response.OutBoundInfoUser;
import com.movie.authservice.dtos.response.UserInfoToken;
import com.movie.authservice.entities.Role;
import com.movie.authservice.entities.User;
import com.movie.authservice.mappers.UserMapper;
import com.movie.authservice.repositories.RoleRepository;
import com.movie.authservice.repositories.UserRepository;
import com.movie.authservice.repositories.httpClient.OutboundIdentityClientGoogle;
import com.movie.authservice.repositories.httpClient.OutboundInfoUserGoogle;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthService {
    private OutboundIdentityClientGoogle outboundIdentityClientGoogle;
    private OutboundInfoUserGoogle outboundInfoUserGoogle;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private JwtService jwtService;
    @NonFinal
    @Value("${auth.google.client-id}")
    String CLIENT_ID;

    @NonFinal
    @Value("${frontend.host}")
    String FRONT_END;

    @NonFinal
    @Value("${auth.google.client-secret}")
    String CLIENT_SECRET;

    @NonFinal
    @Value("${auth.google.redirect-uri}")
    String REDIRECT_URI;

    @NonFinal
    String GRANT_TYPE = "authorization_code";

    public UserInfoToken userSigninByGoogle(String code) {
        GetTokenGoogleReq getTokenGoogleReq = GetTokenGoogleReq.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(FRONT_END + REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build();
        GetTokenGoogleRes getTokenGoogleRes = outboundIdentityClientGoogle.getToken(
                getTokenGoogleReq);
        OutBoundInfoUser infoUser = outboundInfoUserGoogle.getInfoUser("json", getTokenGoogleRes.getAccessToken());
        User user = userRepository.findById(infoUser.id()).orElseGet(() -> {
            User newUser = User.builder()
                    .id(infoUser.id())
                    .name(infoUser.name())
                    .picture(infoUser.picture())
                    .active(true)
                    .build();
            HashSet<Role> roles = new HashSet<>();
            roleRepository.findById(RoleUser.USER_ROLE).ifPresent(roles::add);
            newUser.setRoles(roles);
            userRepository.save(newUser);
            return newUser;
        });
        return AttachInfoUserWithToken(user);
    }

    public UserInfoToken AttachInfoUserWithToken(User user) {
        UserInfoToken userInfo = UserMapper.INSTANCE.toUserInfoToken(user);
        userInfo.setAccessToken(jwtService.generateToken(user));
        userInfo.setRefreshToken(jwtService.generateToken(user));
        return userInfo;
    }
}