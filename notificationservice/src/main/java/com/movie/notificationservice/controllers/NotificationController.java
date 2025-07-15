// package com.movie.notificationservice.controllers;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.movie.notificationservice.dtos.requests.NotificationEvent;
// import com.movie.notificationservice.dtos.responses.ApiRes;
// import com.movie.notificationservice.services.NotificationService;

// import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/notifications")
// @RequiredArgsConstructor
// public class NotificationController {

// private final NotificationService notificationService;

// @PostMapping
// public ApiRes<String> send(@RequestBody NotificationEvent event) {
// notificationService.sendNotification(event);
// return ApiRes.<String>builder().code(200).message("Success").build();
// }
// }
