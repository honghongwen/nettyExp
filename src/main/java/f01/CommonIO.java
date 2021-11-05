package f01;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 * 普通的磁盘阻塞io，这种io是针对流操作的。不同于nio，nio是针对缓冲区操作的
 */
public class CommonIO {

    // 标准的磁盘io，网络io对应到SocketInputStream
    public static void main(String[] args) {
        URL resource = CommonIO.class.getClassLoader().getResource("hello.txt");
        File file = new File(resource.getFile());
        try {
            FileInputStream fi = new FileInputStream(file);
            int n = -1;
            byte[] bytes = new byte[1024];
            StringBuilder builder = new StringBuilder();
            while ((n = fi.read(bytes)) != -1) {
                builder.append(new String(bytes).trim());
//                单个字符读出对应的ascii码
//                n = fi.read();
//                builder.append((char) n);
            }
            System.out.println(builder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
