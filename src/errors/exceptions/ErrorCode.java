package errors.exceptions;

public enum ErrorCode {

    Entity_Not_found("Entity Not Found", "A1"),
    Invalid_Input("Invalid Input", "A2");


    private String message;
    private String code;

    ErrorCode(String message, String code) {
        this.code = code;
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
    public String getCode() {
        return this.code;
    }
}
