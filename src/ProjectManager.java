//import contact.Contact;
import contact.Contact;
import contact.ContactController;
import contact.ContactRepository;
import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;
import utils.Validator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class ProjectManager extends ProjectManagerSupport {

    private final ContactController contactController;
    private final ContactRepository contactRepository;

    private int sortBy; // 정렬 타입 저장 변수
    private int lastPid; // 마지막 pid 저장 변수

    public ProjectManager() {
        super();
        // 의존성 부여를 위한 객체 생성
        this.contactRepository = new ContactRepository();
        this.contactController = new ContactController(this.contactRepository);
    }

    public void init() {
        try {
            // config_info.txt 내용 불러오기
            List<String> configList = fileHandler.readFile("src/config.txt");
            if (configList.size() != 2) {
                System.out.println("환경 변수 파일 형식에 오류가 있습니다.");
                System.exit(0);
            }

            // config 값 validation
            this.sortBy = Integer.parseInt(configList.get(0));
            this.lastPid = Integer.parseInt(configList.get(1));
            if (this.sortBy < 1 || this.sortBy > 3 || this.lastPid <= 0) {
                System.out.println("환경 변수 파일 형식에 오류가 있습니다.");
                System.exit(0);
            }

            // group_info.txt 내용 불러오기
            List<String> groupInfo = fileHandler.readFile("src/group_info.txt");
            if (groupInfo.size() > 1) {
                System.out.println("그룹 정보 파일 형식에 오류가 있습니다.");
                System.exit(0);
            }
            if (groupInfo.size() == 1) {
                this.contactRepository.setGroupTable(contactMapper.groupInfoToArrayList(groupInfo));
                Collections.sort(this.contactRepository.getGroupTable(), (c1, c2) -> c1.compareTo(c2));
            }

            // phonebook.txt 내용 불러오기
            List<String> dataList = fileHandler.readFile("src/phonebook.txt");
            this.contactRepository.setUserTable(contactMapper.mapListToHashMap(dataList, lastPid, this.contactRepository.getGroupTable()));

            // contactRepository에 주입.
            this.contactRepository.initPhoneNumberSet();

            // 모든 연락처의 무선 전화 번호와 비교 했을 때 겹치는 번호가 있는 지 검사
            List<String> phones = new ArrayList<>();
            for (Contact contact : this.contactRepository.getUserTable().values()) {
                for (String phoneNumbers : contact.getPhoneNumber().getPhoneNumbers()) {
                    if (phoneNumbers.matches(Validator.PHONENUM)) {
                        phones.add(phoneNumbers);
                    }
                }
            }
            for (int i = 0; i < phones.size() - 1; i++) {
                for (int j = i + 1; j < phones.size(); j++) {
                    if (phones.get(i).equals(phones.get(j))) {
                        System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
                        System.exit(0);
                    }
                }
            }

            this.contactRepository.setLastPid(this.lastPid);
            this.contactRepository.setSortBy(this.sortBy);
            this.contactRepository.setSequencedUserTable(this.sortBy);
        } catch(NumberFormatException e) {
            System.out.println("환경변수 파일 형식에 오류가 있습니다.");
            System.exit(0);
        }
    }

    public void bootProgram() {
        // read datas from FILE
        init();
        while (true) {
            try {
                System.out.println("원하시는 작업을 고르시오.");
                System.out.println("1) 연락처 검색   2) 연락처 추가");
                System.out.println("3) 연락처 삭제   4) 연락처 수정");
                System.out.println("5) 그룹 관리    6) 설정");
                System.out.print("(종료하려면 '0'을 입력하십시오.)\n>> ");
                String userInput = getUserInput().trim();
                int userCommand = Integer.parseInt(userInput);
                if (userCommand > 6 || userCommand < 0) throw new InvalidInputException(ErrorCode.Invalid_Input);
                //end clause
                if (userCommand == 0) {
                    System.out.println("프로그램이 종료되었습니다.");
                    break;
                }
                this.contactController.routeRequest(userCommand);
                this.lastPid = this.contactRepository.getLastPid();
                this.sortBy = this.contactRepository.getSortBy();
                //save file
                fileHandler.writeListToFile(this.contactRepository.toStringList(), "src/phonebook.txt");
                fileHandler.writeListToFile(
                        Stream.of(Integer.toString(this.sortBy), Integer.toString(this.lastPid))
                                .collect(Collectors.toList()), "src/config.txt");
                List<String> groupArray = new ArrayList<>();
                String groups = "";
                Collections.sort(this.contactRepository.getGroupTable(), (c1, c2) -> c1.compareTo(c2));
                for(int i=0;i<this.contactRepository.getGroupTable().size();i++) {
                    if(i == 0) {
                        groups += this.contactRepository.getGroupTable().get(i);
                    }
                    else {
                        groups += "|" + this.contactRepository.getGroupTable().get(i);
                    }
                }
                groupArray.add(groups);
                fileHandler.writeListToFile(groupArray, "src/group_info.txt");
                this.contactRepository.setSequencedUserTable(this.sortBy);
            } catch (NumberFormatException e1) {
                System.out.println("잘못된 입력 형식입니다.");
            } catch (ApplicationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }

}
