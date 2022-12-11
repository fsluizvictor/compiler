package app.lexer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import domain.semantic.SymbolTableElement;
import domain.types.EndOfFile;
import domain.types.Error;
import domain.types.FloatConst;
import domain.types.IntegerConst;
import domain.types.Literal;
import domain.types.Tag;
import domain.types.Token;
import domain.types.Word;

public class Lexer {

    public static int line = 1; // contador de linhas
    private char ch = ' '; // caractere lido do arquivo
    private FileReader file;

    private Hashtable<String, SymbolTableElement> words = new Hashtable<String, SymbolTableElement>();

    public Lexer() {
    }

    public Lexer(String fileName) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
        } catch (Exception e) {
            System.out.println("File Not found");
        }

    }

    private void reserve(Word word) {
        SymbolTableElement element = new SymbolTableElement(word, Tag.VOID_SEMANTIC_TAG);
        words.put(word.getLexeme(), element);
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
                    int tempLine = line;
                    String error = "/*";
                    do {
                        readch();
                        error += ch;
                        if (ch == '\n')
                            line++;
                        if (ch == '￿') {
                            Token invalidToken = new Error(tempLine, error);
                            tempLine++;
                            line = tempLine;
                            ch = ' ';
                            return invalidToken;
                        }
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
            SymbolTableElement symbolTableElement = (SymbolTableElement) words.get(s);
            if (symbolTableElement != null) {
                return symbolTableElement.getWord(); // palavra já existe na HashTable
            }
            SymbolTableElement newSymbolTableElement = new SymbolTableElement(new Word(s, Tag.IDENTIFIER),
                    Tag.VOID_SEMANTIC_TAG);
            words.put(s, newSymbolTableElement);
            return newSymbolTableElement.getWord();
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

    public Hashtable<String, SymbolTableElement> getWords() {
        return words;
    }

    public void setWords(Hashtable<String, SymbolTableElement> words) {
        this.words = words;
    }
}
