package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.docs.UserDocs;
import com.example.demo.domain.user.dto.response.GetMeResponse;
import com.example.demo.domain.user.service.UserServiceInterface;
import com.example.demo.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserDocs {

    private final UserServiceInterface userService;

    @GetMapping("/me")
    @Override
    public ResponseEntity<BaseResponse.Success<GetMeResponse>> getMe() {
        GetMeResponse response = userService.getMe();
        return BaseResponse.of(response, HttpStatus.OK.value(), "사용자 정보를 가져왔습니다.");
    }
}