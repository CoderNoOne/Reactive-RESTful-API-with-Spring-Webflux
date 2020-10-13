package com.app.application.service;

import com.app.domain.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieRepository movieRepository;
}
