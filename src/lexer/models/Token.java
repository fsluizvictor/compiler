package lexer.models;

public class Token {
    public final int tag; // constante que representa o token
    public String lexeme;

    public Token(int tag) {
        this.tag = tag;
    }

    public String toString() {
        return "tag: " + this.tag + " - valor: " + (char) this.tag;
    }

    public int getTag() {
        return tag;
    }

    public String getLexeme() {
        return lexeme;
    }
}
