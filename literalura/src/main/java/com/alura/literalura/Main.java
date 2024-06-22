package com.alura.literalura;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    Scanner scanner = new Scanner(System.in);
    Consulta consulta = new Consulta();
    ApiResponse resultado;
    String url;

    List<Book> livrosBuscados = new ArrayList<>();
    List<Author> authorsBuscados = new ArrayList<>();

    String escolha;

    final String URL_BASE = "https://gutendex.com/books?";

    public void exibeMenu() {
        while (true) {
            var menu = """
                    0 - Sair
                    1 - Consultar livro
                    2 - Consultar autores
                    """;

            System.out.println(menu);
            var opcao = scanner.nextLine();


            switch (opcao) {
                case "0":
                    System.out.println("Encerrando aplicação ");
                    return;
                case "1":
                    System.out.println("1 - Buscar livro por titulo: ");
                    System.out.println("2 - Listar todos os livros: ");
                    escolha = scanner.nextLine();
                    if (escolha.equals("1")) {
                        System.out.println("Digite o nome do livro para consulta: ");
                        var nomeLivro = scanner.nextLine();
                        consultarLivro(nomeLivro);
                    } else if (escolha.equals("2")) {
                        listarLivrosBuscados();
                    } else {
                        System.out.println("Opção inválida");
                    }

                    break;

                case "2":
                    System.out.println("1 - Lista de autores: ");
                    System.out.println("2 - Listar autores vivos em determinado ano.: ");
                    escolha = scanner.nextLine();

                    if (escolha.equals("1")) {
                        authorsBuscados.forEach(System.out::println);
                    } else if (escolha.equals("2")) {
                        System.out.println("Digite o ano");
                        var ano = scanner.nextInt();
                        listarAutoresVivos(ano);
                    } else {
                        System.out.println("Opção inválida");
                    }
//                        System.out.println("Digite o nome do autor para consulta: ");
//                        var nomeAutor = scanner.nextLine();
//                        consultarAuthor(nomeAutor);
                    break;

                case "3":
                    System.out.println("Digite o idioma para contar os livros: ");
                    var idioma = scanner.nextLine();
                    contarLivrosPorIdioma(idioma);
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    public void consultarLivro(String nomeLivro) {
        url = URL_BASE + "search=" + formatarTermoBusca(nomeLivro);
        resultado = consulta.buscarDados(url);

        livrosBuscados.clear(); // Limpa a lista antes de adicionar novos resultados
//            System.out.println(resultado.toString());

        if (resultado != null && resultado.getResults() != null) {
            for (Book livro : resultado.getResults()) {
                livrosBuscados.add(livro);

                for (Author author : livro.getAuthors()) {
                    authorsBuscados.add(author);
                }

                System.out.println("Livro encontrado: " + livro.getTitle());
            }
        } else {
            System.out.println("Livro não encontrado");
        }
    }

    public void consultarAuthor(String nomeAutor) {
        url = URL_BASE + "search=" + formatarTermoBusca(nomeAutor);
        resultado = consulta.buscarDados(url);
        System.out.println(resultado.toString());
    }

    private String formatarTermoBusca(String termoBusca) {
        return termoBusca.replaceAll("\\s", "%20");
    }

    public void listarLivrosBuscados() {
        if (livrosBuscados.isEmpty()) {
            System.out.println("Nenhum livro foi buscado ainda.");
        } else {
            System.out.println("Listagem de livros buscados:");
            livrosBuscados.forEach(livro -> {
                System.out.println("Título: " + livro.getTitle());
                System.out.println("Autores:");
                livro.getAuthors().forEach(autor -> {
                    System.out.println("- " + autor.getName());
                });
                System.out.println("Idiomas: " + livro.getLanguages());
                System.out.println("Número de Downloads: " + livro.getDownloadCount());
                System.out.println("-----------------------------------");
            });
        }
    }

    public void listarAutoresVivos(int ano) {
        System.out.println("Autores vivos em " + ano + ":");
        boolean encontrouVivos = false;

        for (Author autor : authorsBuscados) {
            if (autor.getBirth_year() <= ano && (autor.getDeath_year() == 0 || autor.getDeath_year() > ano)) {
                encontrouVivos = true;
                System.out.println("- " + autor.getName());
            }
        }

        if (!encontrouVivos) {
            System.out.println("Nenhum autor vivo encontrado em " + ano);
        }

    }

    public void contarLivrosPorIdioma(String idioma) {
        // Chamar o serviço para contar os livros por idioma
        BookService bookService = null;
        Long quantidade = bookService.contarLivrosPorIdioma(idioma);
        System.out.println("Quantidade de livros em " + idioma + ": " + quantidade);
    }
    //        ApiResponse resultado = consulta.buscarLivros("Austen");
//
//        // Exibir os resultados
//        System.out.println("Resposta da API: " + resultado.toString());
}
