package demo.ru.throwable;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
