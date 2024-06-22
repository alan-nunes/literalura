package com.alura.literalura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private LivroRepository bookRepository;

    @Autowired
    private AutorRepository authorRepository;

    public BookService(AutorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // Método para salvar um livro com autores
    public Book salvarLivroComAutores(Book livro) {
        List<Author> autoresDoLivro = livro.getAuthors();

        // Para cada autor no livro, verifique se já existe no banco de dados
        for (int i = 0; i < autoresDoLivro.size(); i++) {
            Author autor = autoresDoLivro.get(i);

            // Verifique se o autor já possui um ID atribuído (ou seja, já está no banco de dados)
            if (autor.getId() == null) {
                // Se o ID for nulo, salve o autor no banco de dados para obter um ID gerado
                autoresDoLivro.set(i, salvarAutor(autor));
            }
            // Caso contrário, o autor já existe no banco de dados com um ID válido
        }

        // Agora os autores do livro têm IDs válidos (gerados ou existentes no banco de dados)

        // Associe os autores ao livro
        livro.setAuthors(autoresDoLivro);

        // Agora você pode salvar o livro com os autores associados
        return bookRepository.save(livro);
    }

    // Método para salvar um autor no banco de dados
    private Author salvarAutor(Author autor) {
        // Implementação do serviço para salvar autor no banco de dados
        return authorRepository.save(autor);
    }

    public Long contarLivrosPorIdioma(String idioma) {
        return bookRepository.countLivrosPorIdioma(idioma);
    }
}
