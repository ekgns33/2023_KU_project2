import contact.ContactController;
import contact.ContactRepository;
import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectManager extends ProjectManagerSupport {

    private final ContactController contactController;
    private final ContactRepository contactRepository;

    private int sortBy;
    private int lastPid;

    public ProjectManager() {
        super();
        // 의존성 부여를 위한 객체 생성
        this.contactRepository = new ContactRepository();
        this.contactController = new ContactController(this.contactRepository);
    }

    public void init() {

        List<String> dataList = fileHandler.readFile("data.txt");
        this.contactRepository.setUserTable(contactMapper.mapListToHashMap(dataList));
        List<String> configList = fileHandler.readFile("config.txt");
        if(configList.size() < 2) throw new ApplicationException(ErrorCode.File_Integrity_Fail);
        // group_info.txt 내용 불러오기
        List<String> groupInfo = fileHandler.readFile("group_info.txt");
        // groupInfo -> ArrayList로 저장
        this.contactRepository.setGroupTable(contactMapper.groupInfoToArrayList(groupInfo));
        //
        this.sortBy = Integer.parseInt(configList.get(0));
        this.lastPid = Integer.parseInt(configList.get(0));
        this.contactRepository.setLastPid(this.lastPid);
        this.contactRepository.setSortBy(this.sortBy);
    }
    public void bootProgram() {
        // read datas from FILE
        init();
        while(true) {
            try {
                String userInput = getUserInput();
                int userCommand = Integer.parseInt(userInput);

                if(userCommand> 6) throw new InvalidInputException(ErrorCode.Invalid_Input);
                //end clause
                if(userCommand == 0) break;
                this.contactController.routeRequest(userCommand);
                this.lastPid = this.contactRepository.getLastPid();
                this.sortBy = this.contactRepository.getSortBy();
                //save file
                fileHandler.writeListToFile(this.contactRepository.toStringList(), "data.txt");
                fileHandler.writeListToFile(
                        Stream.of(Integer.toString(this.lastPid), Integer.toString(this.sortBy))
                                .collect(Collectors.toList()), "config.txt");
                //save group_info.txt 미구현
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
