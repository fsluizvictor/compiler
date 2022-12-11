package domain.types;

public class IntegerConst extends Token {

    public final int value;

    public IntegerConst(int value) {
        super(Tag.DIGIT);
        this.value = value;
    }

    public String toString() {
        return "IntegerConst - tag: " + this.tag + " - valor: " + value;
    }
}
