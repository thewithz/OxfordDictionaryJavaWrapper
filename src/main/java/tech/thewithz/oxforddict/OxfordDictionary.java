package tech.thewithz.oxforddict;

import exceptions.*;
import requests.*;

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
    
    protected enum Endpoint {
        ENTRIES("entries"),
        INFLECTIONS("inflections"),
        SEARCH("search"),
        WORDLIST("wordlist"),
        STATS("stats"),
        LANGUAGES("languages"),
        FILTERS("filters"),
        LEXICALCATEGORIES("lexicalcategories"),
        REGISTERS("registers"),
        DOMAINS("domains"),
        REGIONS("regions"),
        GRAMMATICALFEATURES("grammaticalFeatures");

        private String endpoint;

        Endpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getEndpoint() {
            return endpoint;
        }
    }

    public final String BASE_URL = "https://od-api-demo.oxforddictionaries.com/api/v1";

    protected Language lang = Language.EN;

    protected String app_key = "";

    protected String app_id = "";

    public void setLanguage(Language lang);

    public void setCredentials(String app_key, String app_id);

    public LemmatronRequestBuilder lemmatron(String word);

    public EntriesRequestBuilder entries(String word);

    public JsonObject thesaurus(String word, boolean antonyms, boolean synonyms);

    public SearchRequestBuilder search(String word);

    public TranslationRequestBuilder translate(String word);

    public WordlistRequestBuilder wordlist(String word);

    public JsonObject sentences(String word);

    public JsonObject request(String word, String url, Endpoint endpoint, HashMap<String, LinkedList<String>> filterMap);

    public JsonObject request(String word, String url, Endpoint endpoint, TranslationRequestBuilder.TranslationPair pair);
}
