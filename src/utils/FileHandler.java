package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private String filePath;

    private List<String> dataList;

    public FileHandler (String filePath) {
        this.filePath = filePath;
        this.dataList = new ArrayList<>();
    }

    /**
     * 파일을 한줄씩읽어서 리스트에 삽입 후 리스트 반환
     *
     * @return 파일의 문자열들을 개행마다 분리하여 리스트로 반환.
     *
     * */
    public List<String> readFile() {

        try {
            FileReader fileReader = new FileReader(this.filePath);
            BufferedReader br = new BufferedReader(fileReader);

            String nextLine;

            while((nextLine = br.readLine()) != null) {
                this.dataList.add(nextLine);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this.dataList;
    }

    /**
     *
     * 애플리케이션이 갖고있던 정보를 file에 저장.
     * @param dataList 문자열로 변환된 Contact객체들의 리스트
     *
     * */
    public void writeListToFile(List<String> dataList) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.filePath));
            for(String nextLine : dataList) {
                bufferedWriter.write(nextLine);
                bufferedWriter.newLine();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
