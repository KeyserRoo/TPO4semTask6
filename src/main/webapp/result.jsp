<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="pjatk.tpoprojekt1.Model.Movie" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" type="text/css" href="styles/styles.css">
</head>
<body>
<div class="fixed-background"></div>
<table>
  <thead>
  <tr>
    <th>Nazwa Filmu</th>
    <th>Reżyser</th>
    <th>Gatunek</th>
    <th>Ocena</th>
    <th>Ograniczenie Wiekowe</th>
    <th>Czas Trwania</th>
    <th>Kraj Produkcji</th>
    <th>Producent</th>
    <th>Data Wypuszczenia</th>
  </tr>
  </thead>
  <tbody>
  <% List<Movie> movies = (List<Movie>) request.getAttribute("movies");
    if (movies != null && !movies.isEmpty()) {
      for (Movie movie : movies) {
  %>
  <tr>
    <td><%= movie.getMovieName()%>
    </td>
    <td><%= movie.getMovieDirector()%>
    </td>
    <td><%= movie.getMovieGenre()%>
    </td>
    <td><%= movie.getMovieScore()%>
    </td>
    <td><%= movie.getMovieRating()%>
    </td>
    <td><%= movie.getMovieLength()%>
    </td>
    <td><%= movie.getMovieCountry()%>
    </td>
    <td><%= movie.getMovieCompany()%>
    </td>
    <td><%= movie.getMovieYear()%>
    </td>
  </tr>
  <% }
  } else { %>
  <tr>
    <td colspan="9">No movies found</td>
  </tr>
  <% } %>
  </tbody>
</table>
</body>
</html>