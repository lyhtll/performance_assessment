package com.example.demo.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "이름은 필수입니다")
        @Size(max = 25, message = "이름은 25자를 초과할 수 없습니다")
        String name,
        @NotBlank(message = "비밀번호는 필수입니다")
        @Size(min = 8, max = 32, message = "비밀번호는 8~32자여야 합니다")
        String password
) {
}

