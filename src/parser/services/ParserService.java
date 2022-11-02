package parser.services;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import lexer.controllers.*;
import lexer.models.*;
import lexer.services.*;

public class ParserService {

    private final LexerService lexer;
    private Word word;
    private Token tok;
    private final List<Tag> listTagEsperadas;
    private boolean debug;
    int error;

    public ParserService(String filename) throws FileNotFoundException {

        lexer = new LexerService(filename);
        listTagEsperadas = new ArrayList<>();
        error = 0;
    }

    public void start() {
        advance();
        
        // implementar a execução do program, de onde começam as funções de acordo com a tabela do Follow

        // fim
        if (error == 0) {
            System.out.println("Análise Sintática feita com sucesso.");
        } else {
            System.out.println("Análise Sintática com "+error+ "erros");
        }
    }

    private void advance() {
        try {
            // lê o arquivo
            tok = lexer.scan();
            if (debug) { 
                // Pega o lexema
                System.out.println(word.getLexeme());
            }
            // se o int do token do scan for igual ao int da Tag
            if (tok.tag == Tag.COMMENT_LINE || tok.tag == Tag.COMMENT_BLOCK) {
                advance();
            }
            else if (tok.tag == Tag.EOF){
                if(tok.tag == Tag.ERROR) {
                    // LexicalError();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // private void LexicalError() {
    //     // Se não for o caractere que ele espera, reportar erro
    //     if () {

    //     }// se ler um caractere inválido, reporta o erro e avança
    //     else if () {
    //         System.out.println("Erro léxico");
    //         advance();
    //     }
    // }

    private boolean eat(int tag, String where) {
        if (tok.tag == tag) {
            advance();
            return true;
        }
    }

    private void error(String where) {
        // reporta o erro sintático, listando o que era esperado de receber e o que foi recebido
        System.out.print("Erro Sintático. Esperado: " + "Recebido: ");
    }

    private void errorRecover(String where) {
        // Tratar os erros para que não parem o programa
        }
    }

}