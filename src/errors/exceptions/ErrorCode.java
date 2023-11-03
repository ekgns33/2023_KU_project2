package errors.exceptions;

public enum ErrorCode {

    Entity_Not_found("Entity Not Found", "A1"),

    File_Integrity_Fail("File Integrity Test Fail", "A2"),
    Invalid_Input("잘못된 입력 형식입니다.", "A3");


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
