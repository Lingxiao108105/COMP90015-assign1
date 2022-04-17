package data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Meanings {

    private String noun;
    private String verb;
    private String participle;
    private String article;
    private String pronoun;
    private String preposition;
    private String adverb;
    private String conjunction;

    @JsonCreator
    public Meanings(@JsonProperty("noun")String noun,
                    @JsonProperty("verb")String verb,
                    @JsonProperty("participle")String participle,
                    @JsonProperty("article")String article,
                    @JsonProperty("pronoun")String pronoun,
                    @JsonProperty("preposition")String preposition,
                    @JsonProperty("adverb")String adverb,
                    @JsonProperty("conjunction")String conjunction) {
        this.noun = noun;
        this.verb = verb;
        this.participle = participle;
        this.article = article;
        this.pronoun = pronoun;
        this.preposition = preposition;
        this.adverb = adverb;
        this.conjunction = conjunction;
    }

    //merge new meanings into old meanings, will replace old meanings
    public void merge(Meanings newMeanings){
        if(newMeanings.noun != null){
            this.noun = newMeanings.noun;
        }
        if(newMeanings.verb != null){
            this.verb = newMeanings.verb;
        }
        if(newMeanings.participle != null){
            this.participle = newMeanings.participle;
        }
        if(newMeanings.article != null){
            this.article = newMeanings.article;
        }
        if(newMeanings.pronoun != null){
            this.pronoun = newMeanings.pronoun;
        }
        if(newMeanings.preposition != null){
            this.preposition = newMeanings.preposition;
        }
        if(newMeanings.adverb != null){
            this.adverb = newMeanings.adverb;
        }
        if(newMeanings.conjunction != null){
            this.conjunction = newMeanings.conjunction;
        }

    }

    public String getNoun() {
        return noun;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getParticiple() {
        return participle;
    }

    public void setParticiple(String participle) {
        this.participle = participle;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getPronoun() {
        return pronoun;
    }

    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }

    public String getPreposition() {
        return preposition;
    }

    public void setPreposition(String preposition) {
        this.preposition = preposition;
    }

    public String getAdverb() {
        return adverb;
    }

    public void setAdverb(String adverb) {
        this.adverb = adverb;
    }

    public String getConjunction() {
        return conjunction;
    }

    public void setConjunction(String conjunction) {
        this.conjunction = conjunction;
    }

    @Override
    public String toString() {
        return "Meanings{" +
                "noun='" + noun + '\'' +
                ", verb='" + verb + '\'' +
                ", participle='" + participle + '\'' +
                ", article='" + article + '\'' +
                ", pronoun='" + pronoun + '\'' +
                ", preposition='" + preposition + '\'' +
                ", adverb='" + adverb + '\'' +
                ", conjunction='" + conjunction + '\'' +
                '}';
    }
}
