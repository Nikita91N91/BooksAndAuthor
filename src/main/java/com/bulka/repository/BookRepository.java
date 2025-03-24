package com.bulka.repository;

import com.bulka.entity.Book;
import com.bulka.utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private Connection connection;

    public BookRepository(Connection connection) {
        this.connection = DatabaseConfig.getConnection();
    }


    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM books")) {
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthorId(resultSet.getLong("author_id"));
                books.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
        return books;
    }

    public Book getBookById(Long id) {
        Book book = new Book();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM books WHERE id =?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    book.setId(resultSet.getLong("id"));
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthorId(resultSet.getLong("author_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
        return book;
    }

    public void createBook(Book book) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO books (title, author_id) VALUES (?,?)")) {
            statement.setString(1, book.getTitle());
            statement.setLong(2, book.getAuthorId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
    }

    public void updateBook(Book book) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE books SET title =?, author_id =? WHERE id =?")) {
            statement.setString(1, book.getTitle());
            statement.setLong(2, book.getAuthorId());
            statement.setLong(3, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
    }

    public void deleteBook(Long id) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM books WHERE id =?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
    }
}

