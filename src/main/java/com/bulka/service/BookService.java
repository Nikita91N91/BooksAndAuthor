package com.bulka.service;

import com.bulka.dto.BookDTO;
import com.bulka.entity.Book;
import com.bulka.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;

public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.getAllBooks();
        List<BookDTO> bookDTOS = new ArrayList<>();
        for (Book book : books) {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setId(book.getId());
            bookDTO.setTitle(book.getTitle());
            bookDTO.setAuthorId(book.getAuthorId());
            bookDTOS.add(bookDTO);
        }
        return bookDTOS;
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.getBookById(id);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthorId(book.getAuthorId());
        return bookDTO;
    }

    public void createBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthorId(bookDTO.getAuthorId());
        bookRepository.createBook(book);
    }

    public void updateBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthorId(bookDTO.getAuthorId());
        bookRepository.updateBook(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteBook(id);
    }
}
