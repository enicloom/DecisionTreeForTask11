import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiangTian on 3/29/15.
 */
public class FileRead {

    public static List<PSeletion> genA(String s) {
        List<PSeletion> result = new ArrayList<>();
        boolean flag = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(s));
            String line = br.readLine();
            while (line != null) {
                if (flag) {
                    result.add(new PSeletion(line.split(",")));
                }
                if (line.equals("@data")) {
                    flag = true;
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<PIntro> genB(String s) {
        List<PIntro> result = new ArrayList<>();
        boolean flag = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(s));
            String line = br.readLine();
            while (line != null) {
                if (flag) {
                    result.add(new PIntro(line.split(",")));
                }
                if (line.equals("@data")) {
                    flag = true;
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
