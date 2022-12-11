package domain.types;

public class Error extends Token {

    private int line;
    private String lexeme;

    public Error(int line, String lexeme) {
        super(Tag.ERROR);
        this.line = line;
        this.lexeme = lexeme;
    }

    public String toString() {
        return "Erro - Caracter Inválido - Tag: " + Tag.ERROR
                + " - linha: " + line
                + " - lexema: " + lexeme;
    }
}
