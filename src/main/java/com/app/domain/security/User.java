package com.app.domain.security;

import com.app.domain.movie.Movie;
import com.app.domain.security.enums.Role;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("users")
public class User extends BaseUser {

    private LocalDate birthDate;
    private List<Movie> favoriteMovies;
    private Role role;

    public Integer getAge() {

        return Optional
                .ofNullable(birthDate)
                .map(value -> Period.between(value, LocalDate.now()).getYears())
                .orElse(null);
    }

    public User(String username, String password, LocalDate birthDate, List<Movie> favoriteMovies) {
        super(username, password);
        this.birthDate = birthDate;
        this.favoriteMovies = favoriteMovies;
        role = Role.ROLE_USER;

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

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
        }

        public void setFavoriteMovies(List<Movie> favoriteMovies) {
            this.favoriteMovies = favoriteMovies;
        }
    }
}
