package tech.thewithz.oxforddict;

import exceptions.*;

public interface OxfordDictionary {

    public enum Language {
        EN, ES, MS, SW, TN, NSO, LV, ID, UR, ZU, RO, HI; 
    };

    public enum Region {

    };

    public Language defaultLanguage = Language.EN;

    public JsonObject setLanguage(String lang) throws UnsupportedLanguageException;

    public JsonObject lemmatron(String word, String... filters);

    // TODO dynamic enum for regions to use so arraylist is an option for parameter
    public JsonObject entries();

}
