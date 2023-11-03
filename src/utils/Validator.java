package utils;

import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;


public final class Validator {
    public static final String KOREANONLY = "^[가-힣]+$"; // 한글만 허용(완전 형태 한글만) 한 글자 이상
    public static final String LOWERCASEANDKOREAN = "^[a-z|가-힣]+$"; // 한글, 영어 소문자 허용
    public static final String PHONENUMBERPREFIX = "^01[0-9]*$"; // 01로 시작하는 지(무선 전화의 형식을 취하는지)
    public static final String DIGITONLY = "^[0-9]{9,11}$"; // 숫자로만 9 이상 11 이하 길이
    public static final String PHONENUM = "^01[0-9]{9}$"; // 01로 시작하고 뒤에 9개의 숫자(무선 전화)
    public static final String OFFICENUM = "^[0-9]{9,10}"; // 유선 전화인지

    public Validator() {
    }
    /**
     * @param name : 유저 이름
     * @return 1: valid한 유저이름, return 0  정규표현식에 걸리는 유저이름일 시 반환
     * */
    public static int isValidNameFormat(String name) {
        try {
            if(!name.matches(LOWERCASEANDKOREAN)) {
                throw new InvalidInputException(ErrorCode.Invalid_Input);
            }
        } catch(ApplicationException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        return 1;
    }
    public static int isValidPhoneNumberFormat(String phoneNumber) {
        try {
            // 전화번호가 숫자로만 이루어져 있지 않거나 길이 충족 못 할 시
            if (!phoneNumber.matches(DIGITONLY)) {
                throw new InvalidInputException(ErrorCode.Invalid_Input);
            } else {
                // 무선 전화 형식 체크
                if (!phoneNumber.matches(PHONENUM)) {
                    // 무선 전화 형식은 아니지만 01로 시작하는 지 체크
                    if (phoneNumber.matches(PHONENUMBERPREFIX)) {
                        // 01로 시작하니까 유선 전화도 아님 Error
                        throw new InvalidInputException(ErrorCode.Invalid_Input);
                    }
                    // 무선 전화가 아니라면 유선 전화 형식 체크
                    else {
                        if (!phoneNumber.matches(OFFICENUM)) {
                            // 둘 다 아닐 시 Error
                            throw new InvalidInputException(ErrorCode.Invalid_Input);
                        }
                    }
                }
            }
        }catch(ApplicationException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        return 1;
    }
    public static int isValidGroupNameFormat(String groupName) {
        try {
            // 완전한 형태의 한글로 이루어져 있지 않을 시(중간 공백 허용 X)
            if(!groupName.matches(KOREANONLY)) {
                throw new InvalidInputException(ErrorCode.Invalid_Input);
            }
        } catch(ApplicationException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        return 1;
    }

}
