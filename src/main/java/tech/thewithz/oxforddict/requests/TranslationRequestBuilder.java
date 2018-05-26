package tech.thewithz.oxforddict.requests;

import org.json.JSONObject;
import tech.thewithz.oxforddict.OxfordDictionary;
import tech.thewithz.oxforddict.TranslationPair;
import tech.thewithz.oxforddict.impl.OxfordDictionaryImpl;

public class TranslationRequestBuilder extends RequestBuilder {

    public TranslationRequestBuilder(String word, OxfordDictionaryImpl dict) {
        super(word, dict);
    }

    public TranslationRequestBuilder setTranslationPair(TranslationPair pair) {
        if (pair == null) throw new IllegalArgumentException("LanguagePair cannot be null");
        this.pair = pair;
        return this;
    }

    public JSONObject build() {
        return dict.request(OxfordDictionary.BASE_URL + "/" + OxfordDictionary.Endpoint.ENTRIES.getEndpoint() + "/" + pair.getSourceIANAcode() + "/" + word +
                                    "/translations=" + pair.getTargetIANAcode());
    }

}