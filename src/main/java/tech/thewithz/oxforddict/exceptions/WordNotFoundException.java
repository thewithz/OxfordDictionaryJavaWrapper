public class WordNotFoundException extends Exception {
    public WordNotFoundException() {

    }

    public WordNotFoundException(String message) {
        super(message);
    }

    public WordNotFoundException(Throwable cause) {
        super(cause);
    }

    public WordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
