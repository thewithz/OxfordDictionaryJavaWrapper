public class LemmatronRequestBuilder {
    private OxfordDictionary dict;
    private HashMap<String, LinkedList<String>> filterMap = new HashMap<String, LinkedList<String>>();

    public LemmatronRequestBuilder(OxfordDictionary dict){
        this.dict = dict;
    }

    public LemmatronRequestBuilder addGrammaticalFeatureFilters(String... filters) {
        addFiltersToMap("grammaticalFeatures", filters);
        return this;
    }

    public LemmatronRequestBuilder addLexicalCategoryFilters(String... filters) {
        addFiltersToMap("lexicalCategory", filters);
        return this;
    }

    private void addFiltersToMap(String filterName, String... filters) {
        if(filters.length == 0) throw new IllegalArgumentException("You  must enter at least one filter.");
        if(!filterMap.getKeys().contains(filterName))
            filterMap.put(filterName, new LinkedList<String>(filters));
        else
            filters.forEach(filter -> filterMap.get(filterName).add(filter));
    }

    public JsonObject build(String word) {
        return dict.request(word, dict.BASE_URL, dict.Endpoint.INFLECTIONS, filterMap);
    }
}
