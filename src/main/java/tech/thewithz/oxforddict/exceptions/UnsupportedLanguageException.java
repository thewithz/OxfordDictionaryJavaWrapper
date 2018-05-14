public class UnsupportedLanguageException extends Exception {
    public UnsupportedLanguageException() {

    }

    public UnsupportedLanguageException(String message) {
        super(message);
    }

    public UnsupportedLanguageException(Throwable cause) {
        super(cause);
    }

    public UnsupportedLanguageException(String message, Throwable cause) {
        super(message, cause);
    }
}
