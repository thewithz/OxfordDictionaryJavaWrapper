import unittest
from sys import argv


from oxforddict import OxfordDictionary


class TestOxfordDictLib(unittest.TestCase):


    def tearDown(self):
        self.o.close()


    def setUp(self):
        self.o = OxfordDictionary(**credentials)


    def test_check_lang(self):
        self.assertTrue(self.o._check_lang('en'))
        self.assertTrue(self.o._check_lang('lv'))
        self.assertTrue(self.o._check_lang('es'))

        self.assertFalse(self.o._check_lang('ru'))
        self.assertFalse(self.o._check_lang('asd'))
        self.assertFalse(self.o._check_lang('cn'))


    def test_parse_word(self):
        pass


    def test__request(self):
        pass


    def test_lemmatron(self):
        pass


    def test_lemmatron_with_filters(self):
        pass
        

    def test_entries(self):
        result = self.o.entries('cat')
        self.assertIn('results', result)
        self.assertEquals(result['results'][0]['id'], 'cat')


    def test_entries_with_regions(self):
        pass


    def test_entries_with_filters(self):
        pass


    def test_thesaurus(self):
        pass


if __name__ == '__main__':
    if len(argv[1:]) == 0 or '-h' in argv or '--help' in argv:
        print("Usage: {} app_key='YOUR APP KEY' app_id='YOUR APP ID'".format(argv[0]))
        exit(0)

    credentials = {e.split('=')[0]: e.split('=')[0] for e in argv[1:]}

    unittest.main(argv=argv[:1])
