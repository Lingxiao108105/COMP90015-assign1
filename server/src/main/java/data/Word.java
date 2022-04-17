package data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Word {

    private String spell;
    private Meanings meanings;

    @JsonCreator
    public Word(@JsonProperty("spell")String spell,
                @JsonProperty("meanings")Meanings meanings) {
        this.spell = spell;
        this.meanings = meanings;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public Meanings getMeanings() {
        return meanings;
    }

    public void setMeanings(Meanings meanings) {
        this.meanings = meanings;
    }

    @Override
    public String toString() {
        return "Word{" +
                "spell='" + spell + '\'' +
                ", meanings=" + meanings +
                '}';
    }
}
