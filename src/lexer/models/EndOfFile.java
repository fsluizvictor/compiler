package lexer.models;

public class EndOfFile extends Token {
    private int line;
    private String lexeme;

    public EndOfFile(int line, String lexeme) {
        super(Tag.EOF);
        this.line = line;
        this.lexeme = lexeme;
    }

    public String toString() {
        return "Final de Arquivo - Tag: " + Tag.EOF
                + " - linha: " + line
                + " - lexema: " + lexeme;
    }
}