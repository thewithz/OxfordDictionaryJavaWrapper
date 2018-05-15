public class OxfordDictionaryImpl implements OxfordDictionary {
    
    public OxfordDictionaryImpl() {

    }

    public OxfordDictionaryImpl(String app_key, String app_id) {
        this.app_key = app_key;
        this.app_id = app_id;
    }

    @Override
    public void setLanguage(Language lang) {
        this.lang = lang;
    }

    @Override
    public void setCredentials(String app_key, String app_id) {
        this.app_key = app_key;
        this.app_id = app_id;
    }

    @Override
    public LemmatronRequestBuilder lemmatron(String word) {
        if(word == null) throw new IllegalArgumentException("word cannot be null");
    }

    @Override
    public EntriesRequestBuilder entries(String word) {
        if(word == null) throw new IllegalArgumentException("word cannot be null");
    }

    @Override
    public JsonObject thesaurus(String word, boolean antonyms, boolean synonyms) {
        if(word == null) throw new IllegalArgumentException("word cannot be null");
        if(synonyms == false && antonyms == false) throw new IllegalArgumentException("antonyms and synonyms cannot both be false");
    }

    @Override
    public SearchRequestBuilder search(String word) {
        if(word == null) throw new IllegalArgumentException("word cannot be null");
    }

    @Override
    public TranslationRequestBuilder translate(String word) {
        if(word == null) throw new IllegalArgumentException("word cannot be null");
    }

    @Override
    public WordlistRequestBuilder wordlist(String word) {
        if(word == null) throw new IllegalArgumentException("word cannot be null");
    }

    @Override
    public JsonObject sentences(String word) {
        if(word == null) throw new IllegalArgumentException("word cannot be null");
        if(lang != Language.EN) throw new UnsupportedLanguageException("Sentences are only available with English");
    }

    @Override
    public JsonObject request(String word, String url, Endpoint endpoint, HashMap<String, LinkedList<String>> filterMap) {
        try {
            URL url = new URL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("app_id", app_id);
            conn.setRequestProperty("app_key", app_key);

            if (conn.getResponseCode() != 200) throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) System.out.println(output);

            conn.disconnect();
        }catch(MalformedURLException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JsonObject request(String word, String url, Endpoint endpoint, TranslationRequestBuilder.TranslationPair pair) {

    }

}
