package errors.exceptions;

public class InvalidInputException extends ApplicationException{

    public InvalidInputException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
    public InvalidInputException(ErrorCode errorCode) {
        super(errorCode);
    }
}
