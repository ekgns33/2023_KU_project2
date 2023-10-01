package errors.exceptions;

public class EntityNotFoundException extends ApplicationException{
    public EntityNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
