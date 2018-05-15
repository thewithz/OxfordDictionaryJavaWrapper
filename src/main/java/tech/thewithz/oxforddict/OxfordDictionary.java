package tech.thewithz.oxforddict;

import exceptions.*;

public interface OxfordDictionary {

    public enum Language {
        EN("en", "English"), 
        ES("es", "Spanish"), 
        MS("ms", "Malay"), 
        SW("sw", "Swahili"), 
        TN("tn", "Setswana"), 
        NSO("nso", "Northern Sotho"), 
        LV("lv", "Latvian"), 
        ID("id", "Indonesian"), 
        UR("ur", "Urdu"), 
        ZU("zu", "isiZulu"), 
        RO("ro", "Romanian"), 
        HI("hi", "Hindu");

        private String name;
        private String IANAcode;

        Language(String IANAcode, String name) {
            this.name = name;
            this.IANAcode = IANAcode;
        }

        public String getName() {
            return name;
        }

        public String getIANAcode() {
            return IANAcode;
        }
    }

    private final String BASE_URL = "https://od-api-demo.oxforddictionaries.com/api/v1";

    private Language lang = Language.EN;

    private String app_key = "";

    private String app_id = "";

    public void setLanguage(Language lang);

    public void setCredentials(String app_key, String app_id);

    public LemmatronRequestBuilder lemmatron(String word);

    public EntriesRequestBuilder entries(String word);

    // throws the exception if both antonyms and synonyms are false
    public JsonObject thesaurus(String word, boolean antonyms, boolean synonyms) throws BadRequestException;

    public SearchRequestBuilder search(String word);

    public TranslationRequestBuilder translate(String word);

    public WordlistRequestBuilder wordlist(String word);

    // throws the exception if lang is anything but english
    public JsonObject sentences(String word) throws UnsupportedLanguageException;
}
