package pjatk.tpoprojekt1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pjatk.tpoprojekt1.Model.Movie;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/search")
public class Search extends HttpServlet {
  private final String url = "jdbc:derby:TpoDB;create=true";

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Movie> movies = fetchMovies(request);

    request.setAttribute("movies", movies);
    request.getRequestDispatcher("/result.jsp").forward(request, response);
  }

  private List<Movie> fetchMovies(HttpServletRequest request) {
    List<Movie> movies = new ArrayList<>();
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      Connection connection = DriverManager.getConnection(url);

      String selectQuery = makeQuery(request);
      ResultSet resultSet = getResultSet(request, selectQuery, connection);

      while (resultSet.next()) {
        Movie movie = new Movie();
        movie.setMovieName(resultSet.getString("movieName"));
        movie.setMovieScore(resultSet.getBigDecimal("score"));
        movie.setMovieRating(resultSet.getString("ratingName"));
        movie.setMovieGenre(resultSet.getString("genreName"));
        movie.setMovieYear(resultSet.getInt("relaseYear"));
        movie.setMovieDirector(resultSet.getString("director"));
        movie.setMovieCountry(resultSet.getString("countryName"));
        movie.setMovieCompany(resultSet.getString("company"));
        movie.setMovieLength(resultSet.getInt("movieLength"));
        movies.add(movie);
      }

      connection.close();

    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return movies;
  }

  private ResultSet getResultSet(HttpServletRequest request, String query, Connection connection) throws SQLException, ClassNotFoundException {
    String requestName = request.getParameter("name");
    String requestGenre = request.getParameter("genre");
    String requestDirector = request.getParameter("director");

    PreparedStatement statement = connection.prepareStatement(query);

    int index = 1;
    if (requestName != null && !requestName.isEmpty()) statement.setString(index++, "%"+requestName+"%");
    if (requestGenre != null && !requestGenre.isEmpty()) statement.setString(index++, requestGenre);
    if (requestDirector != null && !requestDirector.isEmpty()) statement.setString(index, "%"+requestDirector+"%");

    return statement.executeQuery();
  }

  private String makeQuery(HttpServletRequest request) {
    String requestName = request.getParameter("name");
    String requestGenre = request.getParameter("genre");
    String requestDirector = request.getParameter("director");
    String requestSortScore = request.getParameter("scoreOrder");

    StringBuilder selectQuery = new StringBuilder();
    selectQuery.append("SELECT m.movieName, m.score,")
        .append(" r.ratingName, g.genreName, c.countryName, m.relaseYear,")
        .append(" m.director, m.company, m.movieLength ")
        .append("FROM MOVIE m ")
        .append("JOIN RATING r ON m.Rating_id = r.id ")
        .append("JOIN GENRE g ON m.Genre_id = g.id ")
        .append("JOIN COUNTRY c ON m.Countries_id = c.id ")
        .append("WHERE 1=1");

    if (requestName != null && !requestName.isEmpty()) selectQuery.append(" AND m.movieName LIKE ?");
    if (requestGenre != null && !requestGenre.isEmpty()) selectQuery.append(" AND g.genreName = ?");
    if (requestDirector != null && !requestDirector.isEmpty()) selectQuery.append(" AND m.director LIKE ?");

    if (requestSortScore != null && requestSortScore.equals("desc")) selectQuery.append(" ORDER BY m.score DESC");
    else selectQuery.append(" ORDER BY m.score ASC");

    return selectQuery.toString();
  }
}