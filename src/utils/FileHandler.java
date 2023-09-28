package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private String filePath;

    private List<String> dataList;

    public FileHandler (String filePath) {
        this.filePath = filePath;
        this.dataList = new ArrayList<>();
    }

    // txt파일을 read하고 한줄 한줄을 list에 담아서 리턴한다.
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

}
