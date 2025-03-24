package com.bulka;

import com.bulka.dto.AuthorDTO;
import com.bulka.repository.AuthorRepository;
import com.bulka.service.AuthorService;
import com.bulka.utils.DatabaseConfig;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        AuthorService authorService = new AuthorService(new AuthorRepository(DatabaseConfig.getConnection()));

        List<AuthorDTO> authors = authorService.getAllAuthors();

        for (AuthorDTO author : authors) {
            System.out.println("ID: " + author.getId());
            System.out.println("Имя: " + author.getName());
            System.out.println("Фамилия: " + author.getSurname());
            System.out.println();
        }
    }
    }

