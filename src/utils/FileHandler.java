package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public FileHandler () {
    }

    /**
     * 파일을 한줄씩읽어서 리스트에 삽입 후 리스트 반환
     *
     * @return 파일의 문자열들을 개행마다 분리하여 리스트로 반환.
     *
     * */
    public List<String> readFile(String filePath) {
        File file = new File(filePath);
        if(!file.exists()) {
            if(filePath.contains("phone"))
                System.out.println("전화번호부 파일이 존재하지 않습니다.");
            else if(filePath.contains("config"))
                System.out.println("환경 변수 파일이 존재하지 않습니다.");
            else if(filePath.contains("group"))
                System.out.println("그룹 정보 파일이 존재하지 않습니다.");
            System.exit(0);
        }
        List<String> dataList = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fileReader);

            String nextLine;

            while((nextLine = br.readLine()) != null) {
                dataList.add(nextLine);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return dataList;
    }

    /**
     *
     * 애플리케이션이 갖고있던 정보를 file에 저장.
     * @param dataList 문자열로 변환된 Contact객체들의 리스트
     *
     * */
    public void writeListToFile(List<String> dataList, String filePath) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            for(String nextLine : dataList) {
                bufferedWriter.write(nextLine);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
