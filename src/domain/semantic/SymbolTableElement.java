package domain.semantic;

import domain.types.Tag;
import domain.types.Word;

public class SymbolTableElement {
    private final Word word;
    private int type = Tag.VOID_SEMANTIC_TAG;

    public SymbolTableElement(Word word, int type) {
        this.word = word;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public Word getWord() {
        return word;
    }

    public void setType(int type) {
        this.type = type;
    }

}
