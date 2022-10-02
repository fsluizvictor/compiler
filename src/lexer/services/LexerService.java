package lexer.services;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import lexer.models.EndOfFile;
import lexer.models.Error;
import lexer.models.FloatConst;
import lexer.models.IntegerConst;
import lexer.models.Literal;
import lexer.models.Tag;
import lexer.models.Token;
import lexer.models.Word;

public class LexerService {

    public static int line = 1; // contador de linhas
    private char ch = ' '; // caractere lido do arquivo
    private FileReader file;

    private Hashtable words = new Hashtable();

    public LexerService() {
    }

    public LexerService(String fileName) throws FileNotFoundException {
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

        keepAllReservedWords();

        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b')
                continue;
            else if (ch == '\n')
                line++;
            else
                break;
        }

        // Recognize comments and math symbols
        switch (ch) {
            case '/':
                if (readch('/')) {
                    do {
                        readch();
                    } while (ch != '\n');
                    line++;
                    ch = ' ';
                    return new Word("//", Tag.COMMENT_LINE);
                }
                if (ch == '*') {
                    do {
                        readch();
                        if (ch == '\n')
                            line++;
                    } while (ch != '*');
                    if (readch('/'))
                        return new Word("/**/", Tag.COMMENT_BLOCK);
                }
                return new Token('/');
            case '+':
                ch = ' ';
                return new Token('+');
            case '-':
                ch = ' ';
                return new Token('-');
            case '*':
                ch = ' ';
                return new Token('*');
            default:
                break;
        }

        switch (ch) {
            // Recognize LOGIC SYMBOLS
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
                if (readch('>'))
                    return Word.ne;
                else
                    ch = ' ';
                return new Token('<');
            case '>':
                if (readch('='))
                    return Word.ge;
                else
                    ch = ' ';
                return new Token('>');
        }

        // Recognize symbols
        switch (ch) {
            case '(':
                ch = ' ';
                return new Token('(');
            case ')':
                ch = ' ';
                return new Token(')');
            case ';':
                ch = ' ';
                return new Token(';');
            case '=':
                ch = ' ';
                return new Token('=');
            case ',':
                ch = ' ';
                return new Token(',');
            default:
                break;
        }

        // Recognize IntegerConst or FloatConst
        if (Character.isDigit(ch)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(ch, 10);
                readch();
            } while (Character.isDigit(ch));

            if (ch == '.') {
                String floatConst = String.valueOf(value) + ch;

                readch();
                value = 0;
                do {
                    value = 10 * value + Character.digit(ch, 10);
                    readch();
                } while (Character.isDigit(ch));

                floatConst += String.valueOf(value);

                return new FloatConst(Float.parseFloat(floatConst));

            }

            return new IntegerConst(value);
        }

        // Identifiers
        if (Character.isLetter(ch)) {
            StringBuffer sb = new StringBuffer();
            do {
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch) || Character.compare(ch, '_') == 0);
            String s = sb.toString();
            Word w = (Word) words.get(s);
            if (w != null)
                return w; // palavra já existe na HashTable
            w = new Word(s, Tag.IDENTIFIER);
            words.put(s, w);
            return w;
        }

        // Literal
        if (ch == '{') {
            String literal = "" + ch;
            do {
                readch();
                literal += ch;
            } while (ch != '}');
            ch = ' ';
            return new Literal(literal);
        }

        if (ch == '￿') {
            Token eof = new EndOfFile(line, String.valueOf(ch));
            return eof;
        }

        // Caracteres não especificados
        Token invalidToken = new Error(line, String.valueOf(ch));
        ch = ' ';
        return invalidToken;
    }

    public void showSymbolsTable() {
        words.values().forEach(word -> System.out.println(word));
    }
}
