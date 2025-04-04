package com.bulka.servlets;

import com.bulka.dto.AuthorDTO;
import com.bulka.repository.AuthorRepository;
import com.bulka.service.AuthorService;
import com.bulka.utils.DatabaseConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "AuthorServlet", urlPatterns = "/BooksAndAuthor/authors")
public class AuthorServlet extends HttpServlet {
    private AuthorService authorService;

    @Override
    public void init() throws ServletException {
        Connection connection = DatabaseConfig.getConnection();
        AuthorRepository authorRepository = new AuthorRepository(connection);
        this.authorService = new AuthorService(authorRepository);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id!= null &&!id.isEmpty()) {
            try {
                AuthorDTO authorDTO = authorService.getAuthorById(Long.parseLong(id));
                PrintWriter writer = resp.getWriter();
                writer.println(authorDTO);
                writer.close();
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            List<AuthorDTO> authors = authorService.getAllAuthors();
            PrintWriter writer = resp.getWriter();
            writer.println(authors);
            writer.close();
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        if (name!= null &&!name.isEmpty() && surname!= null &&!surname.isEmpty()) {
            AuthorDTO authorDTO = new AuthorDTO();
            authorDTO.setName(name);
            authorDTO.setSurname(surname);
            authorService.createAuthor(authorDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        if (id!= null &&!id.isEmpty() && name!= null &&!name.isEmpty() && surname!= null &&!surname.isEmpty()) {
            try {
                AuthorDTO authorDTO = new AuthorDTO();
                authorDTO.setId(Long.parseLong(id));
                authorDTO.setName(name);
                authorDTO.setSurname(surname);
                authorService.updateAuthor(authorDTO);
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id!= null &&!id.isEmpty()) {
            try {
                authorService.deleteAuthor(Long.parseLong(id));
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
