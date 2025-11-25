package com.example.demo.domain.auth.repository;

import com.example.demo.domain.auth.domain.BlacklistToken;
import org.springframework.data.repository.CrudRepository;

public interface BlacklistTokenRepository extends CrudRepository<BlacklistToken, String> {
}

