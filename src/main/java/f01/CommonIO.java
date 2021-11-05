package f01;

import java.io.*;
import java.net.URL;

public class CommonIO {

    public static void main(String[] args) {
        URL resource = CommonIO.class.getClassLoader().getResource("hello.txt");
        File file = new File(resource.getFile());
        try {
            FileInputStream fi = new FileInputStream(file);
            int n = 1;
            StringBuilder builder = new StringBuilder();
            while(n != -1) {
                n = fi.read();
                builder.append((char) n);
            }
            System.out.println(builder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
