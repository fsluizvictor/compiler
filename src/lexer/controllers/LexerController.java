package lexer.controllers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import lexer.models.Word;
import lexer.models.Tag;
import lexer.models.Token;

public class LexerController {

    public static int line = 1; // contador de linhas
    private char ch = ' '; // caractere lido do arquivo
    private FileReader file;

    private Hashtable words = new Hashtable();

    public LexerController(String fileName) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
        } catch (Exception e) {
            System.out.println("File Not found");
        }

    }

    private void reserve(Word word) {
        words.put(word.getLexeme(), word);
    }

    private void keepAllReservedWords() {
        reserve(new Word("program", Tag.PROGRAM));
        reserve(new Word("start", Tag.START));
        reserve(new Word("exit", Tag.EXIT));
        reserve(new Word("int", Tag.INT));
        reserve(new Word("float", Tag.FLOAT));
        reserve(new Word("string", Tag.STRING));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("then", Tag.THEN));
        reserve(new Word("end", Tag.END));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("scan", Tag.SCAN));
        reserve(new Word("print", Tag.PRINT));
    }

    private void readch() throws IOException {
        ch = (char) file.read();
    }

    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c)
            return false;
        ch = ' ';
        return true;
    }

    public Token scan() throws IOException {

        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b')
                continue;
            else if (ch == '\n')
                line++; // conta linhas
            else
                break;
        }

        switch (ch) {
            // Operadores
            case '&':
                if (readch('&'))
                    return Word.and;
                else
                    return new Token('&');
            case '|':
                if (readch('|'))
                    return Word.or;
                else
                    return new Token('|');
            case '=':
                if (readch('='))
                    return Word.eq;
                else
                    return new Token('=');
            case '<':
                if (readch('='))
                    return Word.le;
                else
                    return new Token('<');
            case '>':
                if (readch('='))
                    return Word.ge;
                else
                    return new Token('>');
        }

        return null;
    }

}
