package pjatk.tpoprojekt1;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import pjatk.tpoprojekt1.Database.DataLoader;

@WebServlet(value = "/main-servlet")
public class MainServlet extends HttpServlet {
  private final String url = "jdbc:derby:TpoDB;create=true";
  private Connection connection;

  public void init() {
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      connection = DriverManager.getConnection(url);
      DataLoader.dropTables(connection);
      DataLoader.createTables(connection);
      DataLoader.insertRecords(connection);

    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    try {
      List<String> genres = getElements("genreName", "GENRE");

      request.setAttribute("genre", genres);
      request.getRequestDispatcher("/index.jsp").forward(request, response);

    } catch (ServletException | IOException | ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  private List<String> getElements(String column, String table) throws SQLException, ClassNotFoundException {
    List<String> list = new ArrayList<>();
    PreparedStatement namesStatement = connection.prepareStatement("SELECT " + column + " FROM " + table);
    ResultSet resultSetNames = namesStatement.executeQuery();
    while (resultSetNames.next()) {
      list.add(resultSetNames.getString(column));
    }
    return list;
  }
  public void destroy() {
  }
}