
package com.bulka.repository;

import com.bulka.entity.Author;
import com.bulka.utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {
    private Connection connection;

    public AuthorRepository(Connection connection) {
        this.connection = connection;
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
        if (id == null) {
            throw new NullPointerException("Идентификатор автора не может быть null");
        }
        Author author = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM authors WHERE id =?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    author = new Author();
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
        if (author == null) {
            throw new NullPointerException("Автор не может быть null");
        }
        if (author.getName() == null || author.getSurname() == null) {
            throw new NullPointerException("Имя и фамилия автора не могут быть null");
        }
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO authors (name, surname) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, author.getName());
            statement.setString(2, author.getSurname());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    author.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Не удалось получить идентификатор созданного автора");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
    }

    public void updateAuthor(Author author) {
        if (author == null) {
            throw new NullPointerException("Автор не может быть null");
        }
        if (author.getId() == null) {
            throw new NullPointerException("Идентификатор автора не может быть null");
        }
        if (author.getName() == null || author.getSurname() == null) {
            throw new NullPointerException("Имя и фамилия автора не могут быть null");
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE authors SET name =?, surname =? WHERE id =?")) {
            statement.setString(1, author.getName());
            statement.setString(2, author.getSurname());
            statement.setLong(3, author.getId());
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Автор не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
    }

    public void deleteAuthor(Long id) {
        if (id == null) {
            throw new NullPointerException("Идентификатор автора не может быть null");
        }
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM authors WHERE id =?")) {
            statement.setLong(1, id);
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Автор не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }
    }

    public void deleteAllAuthors() {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM authors")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления авторов", e);
        }
    }

    public Author getAuthorByNameAndSurname(String name, String surname) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM authors WHERE name =? AND surname =?")) {
            statement.setString(1, name);
            statement.setString(2, surname);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Author author = new Author();
                    author.setId(resultSet.getLong("id"));
                    author.setName(resultSet.getString("name"));
                    author.setSurname(resultSet.getString("surname"));
                    return author;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка выполнения запроса", e);
        }
    }
    }
