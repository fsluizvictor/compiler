package app;
import java.util.Scanner;

import app.parser.Parser;

public class runner {
    public static void main(String[] args) {
        while (true) {
            String fullPath = "../compiler/tests/current/";
            Parser parser;
            int testNumber;

            System.out.println("Existem 12 arquivos de teste.\n" +
                    "Os números de 1 a 6 se referem respectivamente a cada teste original(erros sintáticos e léxicos corrigidos) \n" +
                    "Os números de 7 a 12 se referem respectivamente a cada teste com os erros semânticos corrigido \n" +
                    "Digite o número do teste que deseja ser compilado : ");

            Scanner input = new Scanner(System.in);
            testNumber = input.nextInt();

            switch (testNumber) {
                case 1:
                    fullPath += "test1v3.txt";
                    break;
                case 2:
                    fullPath += "test2v3.txt";
                    break;
                case 3:
                    fullPath += "test3v3.txt";
                    break;
                case 4:
                    fullPath += "test4v3.txt";
                    break;
                case 5:
                    fullPath += "test5v3.txt";
                    break;
                case 6:
                    fullPath += "test6v3.txt";
                    break;
                case 7:
                    System.out.println("Teste 1 corrigido: ");
                    fullPath += "test1v4.txt";
                    break;
                case 8:
                    System.out.println("Teste 2 corrigido: ");
                    fullPath += "test2v4.txt";
                    break;
                case 9:
                    System.out.println("Teste 3 corrigido: ");
                    fullPath += "test3v4.txt";
                    break;
                case 10:
                    System.out.println("Teste 4 corrigido: ");
                    fullPath += "test4v4.txt";
                    break;
                case 11:
                    System.out.println("Teste 5 corrigido: ");
                    fullPath += "test5v4.txt";
                    break;
                case 12:
                    System.out.println("Teste 6 corrigido: ");
                    fullPath += "test6v4.txt";
                    break;
                default:
                    break;
            }

            try {
                parser = new Parser(fullPath);
                parser.start();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}
