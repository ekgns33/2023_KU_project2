package utils;

import contact.entity.Contact;


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
    public Map<Integer, Contact> mapListToHashMap(List<String> lines, int lastPid, Set<String> groupTable) {
        Map<Integer, Contact> map = new HashMap<>();
        List<Integer> pids = new ArrayList<>();
        int largestPid=0;
        for(String line : lines) {
            Contact contact = mapStringToContact(line, lastPid, groupTable);
            map.put(contact.getPid(), contact);
            pids.add(contact.getPid());
        }
        // pid 중복 체크
        for(int i=0;i<pids.size()-1;i++) {
            for(int j=i+1;j<pids.size();j++) {
                if(pids.get(i) == pids.get(j)) {
                    System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
                    System.exit(0);
                }
            }
        }
        // last pid 체크
        for(int i=0;i<pids.size();i++) {
            if(pids.get(i) > largestPid)
                largestPid = pids.get(i);
        }
        if(largestPid != lastPid-1) {
            System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
            System.exit(0);
        }
        return map;
    }

    /**
     * 문자열에서 Contact객체 정보를 분리하여 객체 생성.
     * 그룹과 메모정보는 없으면 null로 저장
     * @param line 연락처 정보가 담긴 문자열
     * @return 문자열을 분해해서 만든 Contact 객체
     * */
    public Contact mapStringToContact(String line, int lastPid, Set<String> groupTable) {
        try {
            String[] contactData = line.split("\\|");
            // | 기준으로 잘랐을 때 길이가 최소 4개까지는 나와야 함(pid, 이름, 전번, 그룹
            // test해보니 || 사이에 공백이어도 contactData 안에는 들어가긴 함(""인듯) 그래서 길이 4가 기준임
            // 반대로 메모는 ||로 둘러싸여 있지 않으므로 없으면 그냥 길이가 4가 됨 default 길이는 무조건 4
            if(contactData.length < 4 || contactData.length > 5) {
                System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
                System.exit(0);
            }
            List<String> phoneNumbers = Arrays.stream(contactData[2].split(",")).collect(Collectors.toList());
            // 각 token에 대해 형식 체크
            // pid, 이름, 전화번호 형식 체크
            int checkName, checkGroupName;
            int pid; // pid 검사용 임시 저장 변수
            pid = Integer.parseInt(contactData[0]);
            if (pid < 1 || pid >= lastPid) { // 우선 1보다 작은지만 검사
                System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
                System.exit(0);
            }
            checkName = Validator.isValidNameFormat(contactData[1]);
            if (checkName == -1) {
                System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
                System.exit(0);
            }
            Contact contact = new Contact(Integer.parseInt(contactData[0]), contactData[1]);
            contact.setMemo("");
            for (String phoneNumber : phoneNumbers) {
                if (Validator.isValidPhoneNumberFormat(phoneNumber) == -1 || contact.hasPhoneNumber(phoneNumber)) {
                    System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
                    System.exit(0);
                }

                contact.addPhoneNumber(phoneNumber);
            }
            // 한 연락처 내에 중복된 전화번호 있는 지 체크
            for(int i=0;i<phoneNumbers.size()-1;i++) {
                for(int j=i+1;j<phoneNumbers.size();j++) {
                    if(phoneNumbers.get(i).equals(phoneNumbers.get(j))) {
                        System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
                        System.exit(0);
                    }
                }
            }
            // 그룹 형식 체크
            if (!contactData[3].equals("X")) {
                checkGroupName = Validator.isValidGroupNameFormat(contactData[3]);
                if (checkGroupName == -1) {
                    System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
                    System.exit(0);
                }
                int checkNum = 0;
                // 기존에 설정 되어 있는 그룹명 중 하나인지 확인, 아니면 종료
                if(!groupTable.contains(contactData[3])) {
                    System.out.println("전화번호부 파일 형식에 오류가 있습니다");
                    System.exit(0);
                }
                contact.setGroupName(contactData[3]);
            }
            else {
                contact.setGroupName("X");
            }
            if (contactData.length == 5) {
                if(contactData[4].trim().length() > 20) {
                    System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
                    System.exit(0);
                }
                contact.setMemo(contactData[4].trim());
            }
            return contact;
        } catch (NumberFormatException e) {
            System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
            System.exit(0);
        }
        return null;
    }

    // group_Info.txt 내의 그룹 정보를 ArrayList로 변환해주는 과정
    public Set<String> groupInfoToArrayList(List<String> grouplist){
        List<String> inputList = Arrays.asList(grouplist.get(0).split("\\|"));
        ArrayList<String> groupArrayList = new ArrayList<>(inputList);
        for(String groups : groupArrayList) {
            if(Validator.isValidGroupNameFormat(groups) == -1) {
                System.out.println("그룹 정보 파일 형식에 오류가 있습니다.");
                System.exit(0);
            }
        }
        // 그룹명 중복 체크
        for(int i=0;i<groupArrayList.size()-1;i++) {
            for(int j=i+1;j<groupArrayList.size();j++) {
                if(groupArrayList.get(i).equals(groupArrayList.get(j))) {
                    System.out.println("그룹 정보 파일 형식에 오류가 있습니다");
                    System.exit(0);
                }
            }
        }
        return new HashSet<>(inputList);
    }
}
