package org.example.oop2024_30423_ghile_patricia_project.library;

import org.example.oop2024_30423_ghile_patricia_project.books.Book;
import org.example.oop2024_30423_ghile_patricia_project.connection.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LibraryInventory {
    private final Connection connection;

    public LibraryInventory() {
        this.connection = ConnectionFactory.getConnection();
    }

    public Book createBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            return null;
        }

        //check if a book with the same title already exists
        String checkSql = "SELECT book_id FROM books WHERE title = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, book.getTitle());

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return null;  //book already exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        //continue with the insertion if no duplicate exists
        String sql = "INSERT INTO books (title, author_id, genre_id, price, stock_quantity) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING book_id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId()); //use authorId as integer
            stmt.setInt(3, book.getGenreId());  //use genreId as integer
            stmt.setDouble(4, book.getPrice());
            stmt.setInt(5, book.getStockQuantity());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int generatedBookId = rs.getInt("book_id");
                    book.setBookId(generatedBookId);
                    System.out.println("Book created with ID: " + generatedBookId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    public int getOrCreateAuthorId(String authorName) {
        String selectSql = "SELECT author_id FROM authors WHERE name = ?";
        String insertSql = "INSERT INTO authors (name) VALUES (?) RETURNING author_id";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setString(1, authorName);

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("author_id");
                }
            }
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setString(1, authorName);  //insert the author name

                try (ResultSet rs = insertStmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("author_id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;  //operation failed
    }

    public int getOrCreateGenreId(String genreName) {
        String selectSql = "SELECT genre_id FROM genres WHERE genre_name = ?";
        String insertSql = "INSERT INTO genres (genre_name) VALUES (?) RETURNING genre_id";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setString(1, genreName);

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("genre_id");
                }
            }
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setString(1, genreName);

                try (ResultSet rs = insertStmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("genre_id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }


    //update the stock quantity of an existing book based on book ID or title
    public boolean updateBookStockQuantity(Integer bookId, String title, int newStockQuantity) {
        String sql;

        //if input is book ID, update by book ID
        if (bookId != null) {
            sql = "UPDATE books SET stock_quantity = ? WHERE book_id = ?";
        } else if (title != null && !title.isEmpty()) {
            //if input is title, update by title
            sql = "UPDATE books SET stock_quantity = ? WHERE title ILIKE ?";
        } else {
            //if neither book ID nor title is the input
            return false;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newStockQuantity);  //set the new stock quantity

            //if boook ID
            if (bookId != null) {
                stmt.setInt(2, bookId);
            }
            //if title
            else {
                stmt.setString(2, "%" + title + "%");
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete a book by title and return whether the deletion was successful
    public boolean deleteBookByTitle(String title) {
        String sql = "DELETE FROM books WHERE title ILIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book(s) deleted with title: " + title);
                return true;
            } else {
                System.out.println("No book found with title: " + title);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        //SQL query to join books, authors, and genres tables
        String sql = "SELECT b.book_id, b.title, b.author_id, b.genre_id, b.price, b.stock_quantity, " +
                "a.name AS author_name, " +
                "g.genre_name " +
                "FROM books b " +
                "JOIN authors a ON b.author_id = a.author_id " +
                "JOIN genres g ON b.genre_id = g.genre_id"; //join with authors and genres tables

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                int authorId = rs.getInt("author_id");
                int genreId = rs.getInt("genre_id");
                double price = rs.getDouble("price");
                int stockQuantity = rs.getInt("stock_quantity");

                String authorName = rs.getString("author_name");
                String genreName = rs.getString("genre_name");

                Book book = new Book(bookId, title, authorId, genreId, price, stockQuantity);
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    //get books of a specific genre
    public List<Book> getBooksByGenre(String genreName) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT b.* FROM books b " +
                "JOIN genres g ON b.genre_id = g.genre_id " +
                "WHERE g.genre_name ILIKE ?"; //query for books by genre

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + genreName + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    String title = rs.getString("title");
                    int authorId = rs.getInt("author_id");
                    int genreId = rs.getInt("genre_id");
                    double price = rs.getDouble("price");
                    int stockQuantity = rs.getInt("stock_quantity");

                    Book book = new Book(bookId, title, authorId, genreId, price, stockQuantity);
                    bookList.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public List<Book> searchBooks(String title, String authorName, String genreName) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT b.* FROM books b " +
                "JOIN authors a ON b.author_id = a.author_id " +
                "JOIN genres g ON b.genre_id = g.genre_id " +
                "WHERE b.title ILIKE ? " +
                "AND a.name ILIKE ? " +
                "AND g.genre_name ILIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");
            stmt.setString(2, "%" + authorName + "%");
            stmt.setString(3, "%" + genreName + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    String bookTitle = rs.getString("title");
                    int authorId = rs.getInt("author_id");
                    int genreId = rs.getInt("genre_id");
                    double price = rs.getDouble("price");
                    int stockQuantity = rs.getInt("stock_quantity");

                    Book book = new Book(bookId, bookTitle, authorId, genreId, price, stockQuantity);
                    bookList.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    //sort books by price in ascending order
    public List<Book> sortBooksByPrice() {
        List<Book> books = getAllBooks(); //get all books from the database

        //sort the list by price using a comparator
        books.sort(Comparator.comparingDouble(Book::getPrice));

        return books;
    }

    //get the author name by authorId
    public String getAuthorNameById(int authorId) {
        String sql = "SELECT name FROM authors WHERE author_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //get the genre name by genreId
    public String getGenreNameById(int genreId) {
        String sql = "SELECT genre_name FROM genres WHERE genre_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, genreId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("genre_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //get the username and password from the users table for login
    public boolean authenticateUser(String username, String password) {
        String sql = "SELECT username, password FROM users WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    //get the password from the database
                    String storedPassword = rs.getString("password");

                    //compare the stored password with the provided password
                    if (storedPassword.equals(password)) {
                        return true;  //successful
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  //fail
    }

}