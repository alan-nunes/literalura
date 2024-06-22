package com.alura.literalura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LivroRepository extends JpaRepository<Book, Long> {
    @Query("SELECT COUNT(b) FROM Book b WHERE :idioma MEMBER OF b.languages")
    Long countLivrosPorIdioma(@Param("idioma") String idioma);
}
