package pjatk.tpoprojekt1.Model;

import java.math.BigDecimal;

public class Movie {
    private String movieName;
    private BigDecimal movieScore;
    private String movieRating;
    private String movieGenre;
    private int movieYear;
    private String movieDirector;
    private String movieCountry;
    private String movieCompany;
    private int movieLength;

    public String getMovieName() {
        return movieName;
    }
    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public BigDecimal getMovieScore() {
        return movieScore;
    }
    public void setMovieScore(BigDecimal movieScore) {
        this.movieScore = movieScore;
    }

    public String getMovieRating() {
        return movieRating;
    }
    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieGenre() {
        return movieGenre;
    }
    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    public int getMovieYear() {
        return movieYear;
    }
    public void setMovieYear(int movieYear) {
        this.movieYear = movieYear;
    }

    public String getMovieDirector() {
        return movieDirector;
    }
    public void setMovieDirector(String movieDirector) {
        this.movieDirector = movieDirector;
    }

    public String getMovieCountry() {
        return movieCountry;
    }
    public void setMovieCountry(String movieCountry) {
        this.movieCountry = movieCountry;
    }

    public String getMovieCompany() {
        return movieCompany;
    }
    public void setMovieCompany(String movieCompany) {
        this.movieCompany = movieCompany;
    }

    public int getMovieLength() {
        return movieLength;
    }
    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }
}
