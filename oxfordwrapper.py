import json
import http.client

from .exceptions import *


class OxfordDictionary(object):
    """Simple wrapper for Oxford Dictionary API
    Address: https://www.oxforddictionaries.com/
    API reference: https://developer.oxforddictionaries.com/documentation
    """

    __slots__ = ('app_key', 'app_id', 'lang', '_httpsconn', '_base_url')

    def __init__(self, app_key, app_id, lang='en'):
        """Create OxfordDictionaty object"""
        self.app_key = app_key
        self.app_id = app_id
        self.setlang(lang)
        self._base_url = '/api/v1'
        self._httpsconn = http.client.HTTPSConnection(
            "od-api.oxforddictionaries.com")


    def __repr__(self):
        return "<OxfordDictionary(lang={})>".format(self.lang)


    def __str__(self):
        return self.__repr__()


    def _check_lang(self, lang):
        return ('en', 'es', 'ms', 'sw', 'tn',
                'nso', 'lv', 'id', 'ur', 'zu',
                'ro', 'hi').__contains__(lang.lower())


    def close(self):
        self._httpsconn.close()

    def setlang(self, lang):
        # Check for supported languages
        if not self._check_lang(lang):
            raise UnsupportedLanguageException(0, 'Current language '
                                               'is not supported')
        self.lang = lang


    def _parseword(self, word):
        # Convert unicode cahracters to HTTP form (%c3%b1)
        word = ''.join(map(lambda x: ''.join('%{:x}'.format(i)
            for i in x.encode('utf-8')) if x > 'z' else x, word))
        # Convert word to required form
        return word.strip().lower().replace(' ', '_')


    def _request(self, section, *a, **kw):
        """
        1. section
        2. lang (optional)
        3. word
        4. args
        5. additional args...
        """
        if 'set_lang' in kw:
            set_lang = self.lang
            del kw['set_lang']

        # build request:
        url_args = [section]
        url_params = ''

        if a:
            url_args.extend(a)

        prepared_url = '/'.join([self._base_url, *url_args])

        if kw:
            url_params = '&'.join('{}={}'.format(k, v) for k, v in kw.items())
            prepared_url += '?' + url_params

        self._httpsconn.request('GET', prepared_url,
                                headers={
                                    "Accept": "application/json",
                                    "app_id": self.app_id,
                                    "app_key": self.app_key
                                    }
                                )
        r = self._httpsconn.getresponse()
        # https://developer.oxforddictionaries.com/documentation/response-codes
        if r.status != 200:
            if r.status == 403:
                raise AuthenticationError(r.status, "The request failed due "
                    "to invalid credentials.")
            elif r.status == 404:
                raise WordNotFoundException(r.status, "No information "
                    "available or the requested URL was not found "
                    "on the server.")
            elif r.status == 400:
                raise BadRequestException(r.status, "The request was invalid "
                    "or cannot be otherwise served. An accompanying error "
                    "message will explain further.")
            elif 500 <= r.status <= 504:
                raise ServiceUnavailableError(r.status, "Error on server side")

        return json.loads(r.read().decode('utf-8'))


    def lemmatron(self, word, filters=''):
        """Retrieve available lemmas for a given inflected wordform.
        :word: string with the word
        :*filters: (optional) filter results by categories. Separate
        filtering conditions using a semicolon. Conditions take values
        grammaticalFeatures and/or lexicalCategory and are
        case-sensitive. To list multiple values in single condition
        divide them with comma.

        """

        req_args = ['inflections', self.lang, word]
        if filters:
            req_args.append(filters)

        return self._request(*req_args)


    def entries(self, word, arg='', regions='', filters=''):
        """Retrieve available dictionary entries for a given word and
        language.
        Possible arguments:
        - regions={region}
        - {filters}

        - definitions
        - examples
        - pronunciations

        For example:
        d.entries('cat', 'examples')
        """

        req_args = ['entries', self.lang, word]

        if arg:
            req_args.append(arg)
        if regions:
            req_args.append('regions={}'.format(regions))
        if filters:
            req_args.append('filters={}'.format(filters))

        return self._request(*req_args)

    def thesaurus(self, word, antonyms=False, synonyms=False):
        arg = []
        if antonyms:
            arg.append('antonyms')
        if synonyms:
            arg.append('synonyms')

        return self._request('entries', self.lang, word, ';'.join(arg))


    def search(self, query, translations='', **kw):
        """Retrieve available results for a search query and language.
        :query: Search string.
        :prefix: Set prefix to true if you'd like to get results only
        starting with search string.
        :regions: Filter words with specific region(s) E.g., regions=us.
        :limit: Limit the number of results per response. Default and
        maximum limit is 5000.
        :offset: Offset the start number of the result.

        """
        req_args = ['search', self.lang]
        if translations and self._check_lang(translations):
            req_args.append(
                'translations={}'.format(translations)
            )

        return self._request(*req_args, q=query, **kw)


    def translation(self, word, target_lang):
        """Translate the word to target_lang
        :word: string with the word
        :target_lang: target language
        In case of unsupported target language WordNotFoundException
        will be thrown

        """

        return self._request(
            'entries', self.lang, word, 'translations={}'.format(target_lang)
        )


    def wordlist(self, **filters):
        """Retrieve list of words for particular domain, lexical
        category register and/or region.
        :*filters: Semicolon separated list of wordlist parameters,
        presented as value pairs:

        :limit: Limit the number of results per response. Default and
        maximum limit is 5000.
        :offset: Offset the start number of the result.
        Advanced params:
        :exclude: Semicolon separated list of parameters-value pairs
        (same as filters).
        :exclude_senses: Semicolon separated list of parameters-value
        pairs (same as filters).
        :exclude_prime_sentences: Semicolon separated list of
        parameters-value pairs (same as filters).
        :word_length: Parameter to speficy the minimum (>), exact or
        maximum (<) length
        of the words required.
        :prefix: Filter words that start with prefix parameter
        :exa1ct: If exact=true wordlist returns a list of entries that
        exactly matches the search string.

        LexicalCategory, domains, regions, registers.
        Example for basic filters:
            "registers=Rare;domains=Art"
        Example for advanced filters:
            "lexicalCategory=Noun;domains=sport"

        """
        
        return self._request('wordlist', self.lang, **filters)


    def sentences(self, word):
        """Retrieve list of sentences and list of senses (English
        language only).
        :word: An Entry identifier. Case-sensitive.

        """

        return self._request('entries', self.lang, word, 'sentences')


    def utility_languages(self):
        """Returns a list of monolingual and bilingual language datasets available in the API"""
        
        return self._request('languages')


    def utility_filters(self, endpoint=''):
        """Returns a list of all the valid filters for a given endpoint to construct API calls."""
        
        req_args = ['filters']

        if endpoint:
            req_args.append(endpoint)

        return self._request(*req_args)


    def utility_lexicalcategories(self, endpoint):
        """Returns a list of available lexical categories for a given language dataset."""
        
        return self._request('lexicalcategories', endpoint)


    def utility_registers(self, target_register_language=''):
        """Returns a list of the available registers for a given bilingual language dataset."""

        req_args = ['registers', self.lang]
        
        if target_register_language:
            req_args.append(target_register_language)

        return self._request(*req_args)


    def utility_domains(self, target_domains_language=''):
        """Returns a list of the available domains for a given monolingual language dataset. """

        req_args = ['domains', self.lang]
        if target_domains_language:
            req_args.append(target_domain_language)

        return self._request(*req_args)
        

    def utility_regions(self):
        """Returns a list of the available regions for a given monolingual language dataset."""

        return self._request('regions', self.lang)


    def utility_grammatiocalFeatures(self):
        """Returns a list of the available grammatical features for a given language dataset."""
        
        return self._request('grammaticalFeatures', self.lang)
