package com.bulka.service;

import com.bulka.dto.AuthorDTO;
import com.bulka.entity.Author;
import com.bulka.repository.AuthorRepository;

import java.util.ArrayList;
import java.util.List;

public class AuthorService {

    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.getAllAuthors();
        List<AuthorDTO> authorDTOS = new ArrayList<>();
        for (Author author : authors) {
            AuthorDTO authorDTO = new AuthorDTO();
            authorDTO.setId(author.getId());
            authorDTO.setName(author.getName());
            authorDTO.setSurname(author.getSurname());
            authorDTOS.add(authorDTO);
        }
        return authorDTOS;
    }

    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.getAuthorById(id);
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setSurname(author.getSurname());
        return authorDTO;
    }

    public void createAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setSurname(authorDTO.getSurname());
        authorRepository.createAuthor(author);
    }

    public void updateAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setId(authorDTO.getId());
        author.setName(authorDTO.getName());
        author.setSurname(authorDTO.getSurname());
        authorRepository.updateAuthor(author);
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteAuthor(id);
    }
}
