package tech.thewithz.oxforddict;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import tech.thewithz.oxforddict.exceptions.AuthenticationException;
import tech.thewithz.oxforddict.impl.OxfordDictionaryImpl;

@DisplayName("Testing for OxfordDictionary interface methods.")
public class OxfordDictionaryTest {

    private static OxfordDictionary dict;
    private static OxfordDictionaryImpl dictImpl;

    public OxfordDictionaryTest() {
        dict = new OxfordDictionaryBuilder().setCredentials("1137437bcd5042807e170ab4ba451658", "dd4418a9")
                                            .build();
        dictImpl = new OxfordDictionaryImpl("1137437bcd5042807e170ab4ba451658", "dd4418a9");
    }

    @Test
    @DisplayName("Testing the lemmatron builder.")
    public void lemmatronBuilderTest() {
        Assertions.assertNotNull(dict.lemmatron("cat")
                                     .build());
        Assertions.assertNotNull(dict.lemmatron("cat")
                                     .addLexicalCategoryFilters("noun")
                                     .build());
        Assertions.assertNotNull(dict.lemmatron("can")
                                     .addGrammaticalFeatureFilters("modal")
                                     .build());
        Assertions.assertNotNull(dict.lemmatron("actress")
                                     .addLexicalCategoryFilters("noun")
                                     .addGrammaticalFeatureFilters("feminine")
                                     .build());

    }

    @Test
    @DisplayName("Testing the entries builder.")
    public void entriesBuilderTest() {
        Assertions.assertNotNull(dict.entries("change")
                                     .build());
        Assertions.assertNotNull(dict.entries("change")
                                     .addLexicalCategoryFilters("verb")
                                     .build());
    }

    @Test
    @DisplayName("Testing the thesaurus endpoint")
    public void thesaurusTest() {
        Assertions.assertNotNull(dict.thesaurus("love", true, true));
    }

    @Test
    @DisplayName("Testing the search builder.")
    public void searchBuilderTest() {
        Assertions.assertNotNull(dict.search("cat")
                                     .setLimit(2000)
                                     .setOffset(12)
                                     .setPrefix(true)
                                     .setTranslationPair(TranslationPair.EN_DE)
                                     .build());
    }

    @Test
    @DisplayName("Testing the translate builder.")
    public void translateBuilderTest() {
        Assertions.assertNotNull(dict.translate("cat")
                                     .setTranslationPair(TranslationPair.EN_PT)
                                     .build());
    }

    @Test
    @DisplayName("Testing the wordlist builder.")
    public void wordlistBuilderTest() {
        Assertions.assertNotNull(dict.wordlist()
                                     .addLexicalCategoryFilters("noun", "verb", "adjective")
                                     .setExact(true)
                                     .setPrefix("clear")
                                     .setLimit(2000)
                                     .setOffset(15)
                                     .build());
    }

    @Test
    @DisplayName("Testing the sentences endpoint")
    public void sentencesTest() {
        Assertions.assertNotNull(dict.sentences("orange"));
    }

    @Test(expected = AuthenticationException.class)
    @DisplayName("Testing if invalid credentials throws the correct error.")
    public void invalidCredentialsTest() {
        OxfordDictionary credentialChecker = new OxfordDictionaryBuilder().setCredentials("invalid app key", "invalid app id")
                                                                          .build();
        credentialChecker.lemmatron("cat")
                         .build();
    }

}
