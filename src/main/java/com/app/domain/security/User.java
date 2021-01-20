package com.app.domain.security;

import com.app.domain.movie.Movie;
import com.app.domain.security.enums.Role;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("users")
public final class User extends BaseUser {

    private LocalDate birthDate;
    private List<Movie> favoriteMovies;

    public Integer getAge() {

        return Optional
                .ofNullable(birthDate)
                .map(value -> Period.between(value, LocalDate.now()).getYears())
                .orElse(null);
    }

    public User(String username, String password, LocalDate birthDate, List<Movie> favoriteMovies) {
        super(username, password, Role.ROLE_USER);
        this.birthDate = birthDate;
        this.favoriteMovies = favoriteMovies;
    }

    public static RegularUserBuilder builder() {
        return new RegularUserBuilder();
    }

    public static class RegularUserBuilder {

        private String username;
        private String password;
        private LocalDate birthDate;
        private List<Movie> favoriteMovies;

        public RegularUserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public RegularUserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public RegularUserBuilder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public RegularUserBuilder username(List<Movie> favoriteMovies) {
            this.favoriteMovies = favoriteMovies;
            return this;
        }

        public User build() {
            return new User(username, password, birthDate, favoriteMovies);
        }
    }

    public User addMovieToFavorites(Movie movie) {
        if (isNull(favoriteMovies)) {
            favoriteMovies = new ArrayList<>();
        }
        favoriteMovies.add(movie);
        return this;
    }
}
