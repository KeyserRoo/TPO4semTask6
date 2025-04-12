package pjatk.tpoprojekt1.Database;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
  public static void createTables(Connection connection) {

    try (Statement statement = connection.createStatement()) {
      String country = "CREATE TABLE COUNTRY (" +
          "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
          "countryName VARCHAR(40))";

      String genre = "CREATE TABLE GENRE (" +
          "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
          "genreName VARCHAR(40))";

      String rating = "CREATE TABLE RATING (" +
          "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
          "ratingName VARCHAR(40))";

      String movie = "CREATE TABLE MOVIE (" +
          "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
          "movieName VARCHAR(100)," +
          "score DECIMAL(2,1)," +
          "Rating_id INT," +
          "Genre_id INT," +
          "relaseYear INT," +
          "director VARCHAR(70)," +
          "Countries_id INT," +
          "company VARCHAR(70)," +
          "movieLength INT," +
          "CONSTRAINT fk_movies_ratings FOREIGN KEY (Rating_id) REFERENCES rating (id)," +
          "CONSTRAINT fk_movies_genres FOREIGN KEY (Genre_id) REFERENCES genre (id)," +
          "CONSTRAINT fk_movies_countries FOREIGN KEY (Countries_id) REFERENCES country (id))";

      statement.execute(country);
      statement.execute(genre);
      statement.execute(rating);
      statement.execute(movie);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void insertRecords(Connection connection) {
    try {

      insertFromFile("country.txt", connection);
      insertFromFile("genre.txt", connection);
      insertFromFile("rating.txt", connection);
      insertFromFile("movie.txt", connection);

    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
  }

  private static void insertFromFile(String fileName, Connection connection) throws SQLException, IOException {
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    if (inputStream == null) {
      throw new FileNotFoundException("Cannot find resource: " + fileName);
    }

    String tableName = fileName.substring(0, fileName.length() - 4);
    String insertQuery = getInsertQuery(tableName);

    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    reader.readLine();

    switch (tableName) {
      case "country", "genre", "rating" -> fillOneColumn(reader, preparedStatement);
      case "movie" -> fillMovieRecord(reader, preparedStatement);
      default -> throw new FileNotFoundException();
    }
  }

  private static void fillOneColumn(BufferedReader reader, PreparedStatement preparedStatement) throws IOException, SQLException {
    while (reader.ready()) {
      String line = reader.readLine();
      preparedStatement.setString(1, line);
      preparedStatement.executeUpdate();
    }
  }

  private static void fillMovieRecord(BufferedReader reader, PreparedStatement preparedStatement) throws IOException, SQLException {
    while (reader.ready()) {
      String line = reader.readLine();
      String items[] = line.split(";");
      preparedStatement.setString(1, items[0]);
      preparedStatement.setBigDecimal(2, new BigDecimal(items[1]));
      preparedStatement.setInt(3, Integer.parseInt(items[2])+1);
      preparedStatement.setInt(4, Integer.parseInt(items[3])+1);
      preparedStatement.setInt(5, Integer.parseInt(items[4]));
      preparedStatement.setString(6, items[5]);
      preparedStatement.setInt(7, Integer.parseInt(items[6])+1);
      preparedStatement.setString(8, items[7]);
      preparedStatement.setInt(9, Integer.parseInt(items[8]));
      preparedStatement.executeUpdate();
    }
  }

  private static String getInsertQuery(String tableName) throws FileNotFoundException {
    String insertQuery;
    switch (tableName) {
      case "country" -> insertQuery = "INSERT INTO COUNTRY (countryName) VALUES (?)";
      case "genre" -> insertQuery = "INSERT INTO GENRE (genreName) VALUES (?)";
      case "rating" -> insertQuery = "INSERT INTO RATING (ratingName) VALUES (?)";
      case "movie" ->
          insertQuery = "INSERT INTO MOVIE (movieName, score, Rating_id, Genre_id, relaseYear, director, Countries_id, company, movieLength)" +
              "VALUES (?,?,?,?,?,?,?,?,?)";
      default -> throw new FileNotFoundException();
    }
    return insertQuery;
  }

  public static void dropTables(Connection connection) throws SQLException {
    DatabaseMetaData dbMetaData = connection.getMetaData();
    String[] types = {"TABLE"};
    ResultSet rs = null;

    connection.setAutoCommit(false);
    try (Statement statement = connection.createStatement()) {

      rs = dbMetaData.getTables(null, null, "MOVIE", types);
      if (rs.next()) statement.executeUpdate("DROP TABLE MOVIE");

      rs = dbMetaData.getTables(null, null, "COUNTRY", types);
      if (rs.next()) statement.executeUpdate("DROP TABLE COUNTRY");

      rs = dbMetaData.getTables(null, null, "GENRE", types);
      if (rs.next()) statement.executeUpdate("DROP TABLE GENRE");

      rs = dbMetaData.getTables(null, null, "RATING", types);
      if (rs.next()) statement.executeUpdate("DROP TABLE RATING");

      connection.commit();
    } catch (SQLException e) {
      System.out.println("Error dropping tables: " + e.getMessage());
      connection.rollback();
      throw e;
    } finally {
      connection.setAutoCommit(true);
      rs.close();
    }
  }
}

//  private static void insertFromFile(String path, Connection connection) throws IOException, SQLException {
//    File file = new File(path);
//    BufferedReader reader = new BufferedReader(new FileReader(file));
//
//    Object[] returns = prepareStatement(file, connection);
//    PreparedStatement preparedStatement = (PreparedStatement) returns[0];
//    List<Integer> columnTypes = (List<Integer>) returns[1];
//
//    while (reader.ready()) {
//      String line = reader.readLine();
//      String[] items = line.split(";");
//      List<Object> values = new ArrayList<>();
//
//      for (int i = 0; i < items.length; i++) values.add(items[i]);
//
//      for (int i = 0; i < values.size(); i++)
//        switch (columnTypes.get(i)) {
//          case Types.INTEGER -> preparedStatement.setInt(i + 1, Integer.parseInt((String) values.get(i)));
//          case Types.VARCHAR -> preparedStatement.setString(i + 1, (String) values.get(i));
//          case Types.DECIMAL -> preparedStatement.setBigDecimal(i + 1, (BigDecimal) values.get(i));
//          default -> throw new IllegalArgumentException("Unsupported data type: " + columnTypes.get(i));
//        }
//      preparedStatement.executeUpdate();
//    }
//  }
//
//  private static Object[] prepareStatement(File file, Connection connection) throws IOException, SQLException {
//    String tableName = file.getName().substring(0, file.getName().length() - 4);
//
//    DatabaseMetaData meta = connection.getMetaData();
//    ResultSet rs = meta.getColumns(null, null, tableName, null);
//    List<String> columnNames = new ArrayList<>();
//    List<Integer> columnTypes = new ArrayList<>();
//
//    while (rs.next()) {
//      columnNames.add(rs.getString("COLUMN_NAME"));
//      columnTypes.add(rs.getInt("DATA_TYPE"));
//    }
//    rs.close();
//
//    StringBuilder insertQuery = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
//    for (String columnName : columnNames) insertQuery.append(columnName).append(", ");
//    insertQuery.delete(insertQuery.length() - 2, insertQuery.length()).append(") VALUES (");
//
//    for (int i = 0; i < columnNames.size(); i++) insertQuery.append("?,");
//
//    insertQuery.setLength(insertQuery.length() - 1);
//    insertQuery.append(")");
//
//    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery.toString());
//
//    return new Object[]{preparedStatement, columnTypes};
//  }
//}
//
//    String insertQuery = "";
//    insertQuery = "INSERT INTO " + tableName + " (";
//
//    String[] params = reader.readLine().split(";");
//    for (int i = 0; i < params.length - 1; i++)
//      insertQuery += params[i] + ", ";
//
//    insertQuery += params[params.length - 1] + ") VALUES (";
//    for (int i = 0; i < params.length - 1; i++)
//      insertQuery += "?,";
//
//    insertQuery += "?)";
//    String line = "";
//    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
//
//    while ((line = reader.readLine()) != null) {
//      String[] items = line.split(";");
//      for (int i = 1; i < items.length; i++) {
////        preparedStatement.setString(i, items[i]);
//      }
//    }

