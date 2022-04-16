package Data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Word {

    private String spell;
    private String meaning;

    @JsonCreator
    public Word(@JsonProperty("spell")String spell,
                @JsonProperty("meaning")String meaning) {
        this.spell = spell;
        this.meaning = meaning;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public String toString() {
        return "Word{" +
                "spell='" + spell + '\'' +
                ", meaning='" + meaning + '\'' +
                '}';
    }
}
