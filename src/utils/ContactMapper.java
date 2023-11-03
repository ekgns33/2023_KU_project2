package utils;

import contact.Contact;
import utils.Validator;

import java.util.*;
import java.util.stream.Collectors;


public class ContactMapper {

    public ContactMapper() {
    }

    /**
     * file에서 읽은 문자열을 읽어서 Contact객체를 생성.
     * @param lines 연락처 정보가 담긴 문자열 리스트
     * @return Contact 객체들이 담긴 해시맵.
     *
     * */
    public Map<Integer, Contact> mapListToHashMap(List<String> lines, int lastPid) {
        Map<Integer, Contact> map = new HashMap<>();
        for(String line : lines) {
            Contact contact = mapStringToContact(line, lastPid);
            map.put(contact.getPid(), contact);
        }
        return map;
    }

    /**
     * 문자열에서 Contact객체 정보를 분리하여 객체 생성.
     * 그룹과 메모정보는 없으면 null로 저장
     * @param line 연락처 정보가 담긴 문자열
     * @return 문자열을 분해해서 만든 Contact 객체
     * */
    public Contact mapStringToContact(String line, int lastPid) {
        String[] contactData = line.split("\\|");
        List<String> phoneNumbers = Arrays.stream(contactData[2].split(",")).collect(Collectors.toList());
        // 각 token에 대해 형식 체크
        // pid, 이름, 전화번호 형식 체크
        int checkName, checkPhoneNumber, checkGroupName;
        int pid; // pid 검사용 임시 저장 변수
        pid = Integer.parseInt(contactData[0]);
        if(pid < 1 || pid > lastPid) { // 우선 1보다 작은지만 검사
            System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
            System.exit(0);
        }
        checkName = Validator.isValidNameFormat(contactData[1]);
        if(checkName == -1) {
            System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
            System.exit(0);
        }
        Contact contact = new Contact(Integer.parseInt(contactData[0]), contactData[1]);

        for(String phoneNumber: phoneNumbers) {
            if(Validator.isValidPhoneNumberFormat(phoneNumber) == -1) {
                System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
                System.exit(0);
            }
            contact.addPhoneNumber(phoneNumber);
        }
        // 그룹 형식 체크
        // tokens 길이로 체크하면 안됨
        // 그룹, 메모는 요소가 있든 말든 |는 무조건 있기 때문에 split 될 때
        // tokens[3], tokens[4]가 무조건 존재 이들이 empty이지 않으면 있다는 것으로
        // 인식하고 형식 검사하면 됨
        if(!contactData[3].isEmpty()) {
            checkGroupName = Validator.isValidGroupNameFormat(contactData[3]);
            if(checkGroupName == -1) {
                System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
                System.exit(0);
            }
            contact.setGroupName(contactData[3]);
        }
        if(!contactData[4].isEmpty()) {
            contact.setMemo(contactData[4]);
        }
        return contact;
    }

    // 한줄로 읽어온 Group_Info.txt 내의 그룹 정보를 ArrayList로 변환해주는 과정
    public ArrayList<String> groupInfoToArrayList(List<String> line){
        String token = line.get(0);
        ArrayList<String> groupList = new ArrayList<>();
        String[] groupElements = token.split("\\|");
        for(String groupName : groupElements){
            groupList.add(groupName);
        }
        return groupList;
    }
}
