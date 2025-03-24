package com.bulka.servlets;

import com.bulka.dto.BookDTO;
import com.bulka.repository.BookRepository;
import com.bulka.service.BookService;
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

@WebServlet(name = "BookServlet", urlPatterns = "/BooksAndAuthor/books")
public class BookServlet extends HttpServlet {
    private BookService bookService;

    public BookServlet(BookService bookService) {
        this.bookService = bookService;
    }
    @Override
    public void init() throws ServletException {
        Connection connection = DatabaseConfig.getConnection();
        BookRepository bookRepository = new BookRepository(connection);
        this.bookService = new BookService(bookRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id != null) {
            BookDTO bookDTO = bookService.getBookById(Long.parseLong(id));
            PrintWriter writer = resp.getWriter();
            writer.println(bookDTO);
        } else {
            List<BookDTO> books = bookService.getAllBooks();
            PrintWriter writer = resp.getWriter();
            writer.println(books);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String authorId = req.getParameter("authorId");
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle(title);
        bookDTO.setAuthorId(Long.parseLong(authorId));
        bookService.createBook(bookDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String title = req.getParameter("title");
        String authorId = req.getParameter("authorId");
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(Long.parseLong(id));
        bookDTO.setTitle(title);
        bookDTO.setAuthorId(Long.parseLong(authorId));
        bookService.updateBook(bookDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        bookService.deleteBook(Long.parseLong(id));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
