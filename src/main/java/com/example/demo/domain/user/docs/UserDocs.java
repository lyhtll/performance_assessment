package com.example.demo.domain.user.docs;

import com.example.demo.domain.user.dto.response.GetMeResponse;
import com.example.demo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "사용자 API")
public interface UserDocs {

    @Operation(summary = "사용자 정보 조회", description = "사용자의 정보를 조회합니다.")
    ResponseEntity<BaseResponse.Success<GetMeResponse>> getMe();
}

