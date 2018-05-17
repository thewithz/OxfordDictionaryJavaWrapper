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
    public JsonObject request(String word, Endpoint endpoint, HashMap<String, LinkedList<String>> filterMap) {
        ArrayList<String> filterStrings = new ArrayList<String>(); 
        for(String filter : filterMap.getKeys()) {
            StringBuilder filterBuilder = new StringBuilder();
            filterBuilder.append(filter)
                .append("=")
                .append(filterMap.get(filter)
                        .stream()
                        .map(filterType -> filterType.toString())
                        .collect(Collectors.joining(",")));
            filterStrings.add(filterBuilder.toString());
        }
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(BASE_URL)
            .append("/")
            .append(endpoint)
            .append("/")
            .append(lang.getIANAcode())
            .append("/")
            .append(word)
            .append("/")
            .append(filterStrings.stream()
                    .map(filters -> filters.toString())
                    .collect(Collectors.joining(";")));
        return request(urlBuilder.toString());
    }

    @Override
    public JsonObject request(String word, Endpoint endpoint, TranslationRequestBuilder.TranslationPair pair) {

    }

    private JsonObject request(String url){
        StringBuilder jsonBuilder = new StringBuilder();
        try {
            URL url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("app_id", app_id);
            conn.setRequestProperty("app_key", app_key);

            switch(conn.getResponseCode()){
                case 200: break; // no errors
                case 403: throw new AuthenticationException("The request failed due to invalid credentials.");
                case 404: throw new WordNotFoundException("No information found, or the requested URL was not found on the server.");
                case 400: throw new BadRequestException("The request was invalid or cannot be otherwise served. An accompanying error message will explain further.");
                case 500:
                case 501:
                case 502:
                case 503:
                case 504: throw new ServiceUnavailableException("The Oxford Dictionary service is currently unreachable.");
                default: break;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) jsonBuilder.append(output).append("\n");

            conn.disconnect();
        }catch(MalformedURLException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return new JsonObject(jsonBuilder.toSring());
    }

}
