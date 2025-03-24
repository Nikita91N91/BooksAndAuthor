package com.bulka.repository;

import com.bulka.entity.Author;
import com.bulka.utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {
    private Connection connection;

    public AuthorRepository(Connection connection) {
        this.connection = DatabaseConfig.getConnection();
    }

    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM authors")) {
            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setName(resultSet.getString("name"));
                author.setSurname(resultSet.getString("surname"));
                authors.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
        return authors;
    }

    public Author getAuthorById(Long id) {
        Author author = new Author();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM authors WHERE id =?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    author.setId(resultSet.getLong("id"));
                    author.setName(resultSet.getString("name"));
                    author.setSurname(resultSet.getString("surname"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
        return author;
    }

    public void createAuthor(Author author) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO authors (name, surname) VALUES (?,?)")) {
            statement.setString(1, author.getName());
            statement.setString(2, author.getSurname());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
    }

    public void updateAuthor(Author author) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE authors SET name =?, surname =? WHERE id =?")) {
            statement.setString(1, author.getName());
            statement.setString(2, author.getSurname());
            statement.setLong(3, author.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
    }

    public void deleteAuthor(Long id) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM authors WHERE id =?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
    }
}
