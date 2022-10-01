package lexer.controllers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.Hashtable;
import java.util.Scanner;

import lexer.models.Word;
import lexer.services.LexerService;
import lexer.models.Tag;
import lexer.models.Token;

public class LexerController {
    public static void main(String[] args) {

        String fullPath = "/home/victor/Projects/compiler/tests/lexerTests/";

        LexerService service = null;

        while (true) {

            System.out.println("Temos 6 arquivos de teste,\n" +
                    "os números de 1 a 6 se referem respectivamente a cada teste\n" +
                    "Digite o número do teste que deseja ser compilado : ");

            Scanner input = new Scanner(System.in);
            int testNumber = input.nextInt();

            switch (testNumber) {
                case 1:
                    fullPath += "test1.txt";
                    break;
                case 2:
                    fullPath += "test2.txt";
                    break;
                case 3:
                    fullPath += "test3.txt";
                    break;
                case 4:
                    fullPath += "test4.txt";
                    break;
                case 5:
                    fullPath += "test5.txt";
                    break;
                case 6:
                    fullPath += "test6.txt";
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

            try {
                Token token = service.scan();
            } catch (Exception e) {
                // TODO: handle exception
            }

        }

    }
}
