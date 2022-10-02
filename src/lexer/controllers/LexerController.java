package lexer.controllers;

import java.util.Scanner;

import lexer.services.LexerService;
import lexer.models.EndOfFile;
import lexer.models.Token;

public class LexerController {
    public static void main(String[] args) {

        String fullPath = "/home/victor/Projects/compiler/tests/lexerTests/";
        LexerService service = null;
        int testNumber = 1;

        System.out.println("Temos 11 arquivos de teste.\n" +
                "Os números de 1 a 6 se referem respectivamente a cada teste original(sujeito a ter erros) \n" +
                "Os números de 7 a 11 se referem respectivamente a cada teste corrigido \n" +
                "Digite o número do teste que deseja ser compilado : ");

        Scanner input = new Scanner(System.in);
        testNumber = input.nextInt();

        switch (testNumber) {
            case 1:
                fullPath += "test1v2.txt";
                break;
            case 2:
                fullPath += "test2v2.txt";
                break;
            case 3:
                fullPath += "test3v2.txt";
                break;
            case 4:
                fullPath += "test4v2.txt";
                break;
            case 5:
                fullPath += "test5v2.txt";
                break;
            case 6:
                fullPath += "test6.txt";
            case 7:
                fullPath += "test1.txt";
                break;
            case 8:
                fullPath += "test2.txt";
                break;
            case 9:
                fullPath += "test3.txt";
                break;
            case 10:
                fullPath += "test4.txt";
                break;
            case 11:
                fullPath += "test5.txt";
                break;
            default:
                break;
        }

        try {
            service = new LexerService(fullPath);
        } catch (Exception e) {
            // TODO: handle exception
        }

        System.out.println("Reconhecimento dos tokens");

        while (true) {
            try {
                Token token = service.scan();
                if (token instanceof EndOfFile) {
                    break;
                } else {
                    System.out.println(token.toString());
                }
            } catch (Exception e) {
                System.out.println("Error with read file!");
                break;
            }
        }
        System.out.println("Tabela de Símbolos : ");
        service.showSymbolsTable();
    }
}
