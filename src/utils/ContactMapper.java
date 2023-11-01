package utils;

import contact.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactMapper {

    public ContactMapper() {}

    /**
     * file에서 읽은 문자열을 읽어서 Contact객체를 생성.
     * @param lines 연락처 정보가 담긴 문자열 리스트
     * @return Contact 객체들이 담긴 해시맵.
     *
     * */
    public Map<Integer, Contact> mapListToHashMap(List<String> lines) {
        Map<Integer, Contact> map = new HashMap<>();

        for(String line : lines) {
            Contact contact = mapStringToContact(line);
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
    public Contact mapStringToContact(String line) {
        String[] tokens = line.split("\\|");
        Contact contact = new Contact(Integer.parseInt(tokens[0]), tokens[1], tokens[2]);
        if(tokens.length >= 4) {
            contact.setGroupName(tokens[3]);
        }
        if(tokens.length == 5) {
            contact.setMemo(tokens[4]);
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
