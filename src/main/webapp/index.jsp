<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="pjatk.tpoprojekt1.Model.Movie" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="styles/styles.css">
    <title>Wyszukiwarka Filmów</title>
</head>
<body>
<div class="search-container">
    <h1>Wyszukiwarka Filmów</h1>
    <form id="search-form" action="search" method="GET">
        <input type="text" name="name" placeholder="Nazwa filmu"><br>
        <input type="text" name="director" placeholder="Imię reżysera"><br>
        <select name="genre">
            <option value="">Gatunek filmu</option>
            <%
                List<String> genres = (List<String>) request.getAttribute("genre");
                if (genres!= null &&!genres.isEmpty()) {
                    for (String genre : genres) {
            %>
            <option value="<%= genre %>"><%= genre %></option>
            <%
                }
            } else {
            %>
            <option value="">Brak dostępnych gatunków</option>
            <% } %>
        </select><br>
        <select name="scoreOrder">
            <option value="">Bez Sortowania</option>
            <option value="asc">Oceny Rosnąco</option>
            <option value="desc">Oceny Malejąco</option>
        </select><br>
        <input type="submit" value="Search">
    </form>
</div>
<script src="script.js"></script>
</body>
</html>