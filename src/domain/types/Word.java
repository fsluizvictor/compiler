package domain.types;

public class Word extends Token {
    private String lexeme = "";

    public static final Word and = new Word("&&", Tag.AND);
    public static final Word or = new Word("||", Tag.OR);
    public static final Word eq = new Word("==", Tag.EQ);
    //public static final Word ne = new Word("!=", Tag.NE);
    public static final Word le = new Word("<=", Tag.LE);
    public static final Word ge = new Word(">=", Tag.GE);
    public static final Word ne = new Word(">=", Tag.NE);

    public Word(String lexeme, int tag) {
        super(tag);
        this.lexeme = lexeme;
    }

    @Override
    public String getLexeme() {
        return lexeme;
    }

    public String toString() {
        return "Word - tag: " + this.tag + " - valor: " + lexeme;
    }

}
