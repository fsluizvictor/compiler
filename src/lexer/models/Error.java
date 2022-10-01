package lexer.models;

public class Error extends Token {

    private int line = Integer.MIN_VALUE;
    private String lexeme;

    public Error(int line, String lexeme) {
        super(Tag.ERROR);
        line = line;
        lexeme = lexeme;
    }

    public String toString() {
        return "Erro - Caracter Inválido - Tag : " + Tag.ERROR
                + "linha : " + line
                + "lexeme : " + lexeme;
    }
}
