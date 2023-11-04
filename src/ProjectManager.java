import contact.ContactController;
import contact.ContactRepository;
import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;
import java.util.List;
import java.util.Scanner;
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

        // config_info.txt 내용 불러오기
        List<String> configList = fileHandler.readFile("./config.txt");
        if (configList.size() < 2) throw new ApplicationException(ErrorCode.File_Integrity_Fail);

        // config 값 validation
        this.sortBy = Integer.parseInt(configList.get(0));
        this.lastPid = Integer.parseInt(configList.get(1));
        if(this.sortBy < 1 || this.sortBy > 3 || this.lastPid < 0) {
            System.out.println("환경 변수 파일 형식에 오류가 있습니다.");
            System.exit(0);
        }


        // phonebook.txt 내용 불러오기
        List<String> dataList = fileHandler.readFile("./phonebook.txt");
        this.contactRepository.setUserTable(contactMapper.mapListToHashMap(dataList, lastPid));

        // group_info.txt 내용 불러오기
        List<String> groupInfo = fileHandler.readFile("./group_info.txt");
        this.contactRepository.setGroupTable(contactMapper.groupInfoToArrayList(groupInfo));

        // contactRepository에 주입.
        this.contactRepository.initPhoneNumberSet();
        this.contactRepository.setLastPid(this.lastPid);
        this.contactRepository.setSortBy(this.sortBy);
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
                String userInput = getUserInput();
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
                fileHandler.writeListToFile(this.contactRepository.toStringList(), "./phonebook.txt");
                fileHandler.writeListToFile(
                        Stream.of(Integer.toString(this.sortBy), Integer.toString(this.lastPid))
                                .collect(Collectors.toList()), "./config.txt");
                fileHandler.writeListToFile(this.contactRepository.getGroupTable(), "./group_info.txt");
                //save group_info.txt 미구현
            } catch (NumberFormatException e1) {
                System.out.println("다시 입력해주세요.");
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
