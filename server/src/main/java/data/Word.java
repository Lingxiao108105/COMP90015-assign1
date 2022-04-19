package data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * contains spell and meanings of a word
 *
 * @author lingxiao li 1031146
 */
public class Word {

    private String spell;
    private Meanings meanings;

    @JsonCreator
    public Word(@JsonProperty("spell")String spell,
                @JsonProperty("meanings")Meanings meanings) {
        this.spell = spell.toLowerCase();
        this.meanings = meanings;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell.toLowerCase();
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
