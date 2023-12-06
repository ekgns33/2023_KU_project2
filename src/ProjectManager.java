import contact.ContactController;
import contact.repositories.ContactRepository;
import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;

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
        this.contactRepository = ContactRepository.getInstance();
        this.contactController = new ContactController();
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
            if (this.sortBy < 1 || this.sortBy > 2 || this.lastPid <= 0) {
                System.out.println("환경 변수 파일 형식에 오류가 있습니다.");
                System.exit(0);
            }

            // group_info.txt 내용 불러오기
            List<String> groupInfo = fileHandler.readFile("src/group_info.txt");
            if (groupInfo.size() > 1) {
                System.out.println("그룹 정보 파일 형식에 오류가 있습니다.");
                System.exit(0);
            }

            // 암것도 없을 때... ->
            if(!groupInfo.isEmpty()) {
                List<String> groupList = contactMapper.groupInfoToArrayList(groupInfo);
                this.contactRepository.initGroupMap(groupList);
                this.contactRepository.setGroupTable(new HashSet<String>(groupList));
            }

            // phonebook.txt 내용 불러오기
            List<String> dataList = fileHandler.readFile("src/phonebook.txt");
            this.contactRepository.setUserTable(contactMapper.mapListToHashMap(dataList, lastPid, contactRepository.getGroupTable()));

            // contactRepository에 주입.
            this.contactRepository.initPhoneNumberSet();

            this.contactRepository.setLastPid(this.lastPid);
            this.contactRepository.setSortBy(this.sortBy);
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
                StringBuilder sb = new StringBuilder();
                int cnt = 0;
                for(String groupName : this.contactRepository.getGroupTable()) {
                    if(cnt != 0) {
                        sb.append("|");
                    }
                    sb.append(groupName);
                    cnt++;
                }
                groupArray.add(sb.toString());
                fileHandler.writeListToFile(groupArray, "src/group_info.txt");
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
