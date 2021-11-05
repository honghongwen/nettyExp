package f02;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOChannel_copyFile {

    public static void main(String[] args) {
        URL url = NIOChannel_copyFile.class.getResource("/hello.txt");
        File srcFile = new File(url.getFile());

        File destFile = new File("/Users/honghongwen/Workspace/Tanzhou/nipExp/src/main/resources/dest.txt");
        try {
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileChannel srcChannel = new FileInputStream(srcFile).getChannel();
            FileChannel destChannel = new FileOutputStream(destFile).getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while(srcChannel.read(buffer) != -1) {
                // 对于channel是read,但是对于buffer，则是写模式，翻转成读模式
                buffer.flip();
                int length = 0;
                while ((length = destChannel.write(buffer)) != 0) {
                    System.out.println("写入的字节数" + length);
                }

                // 再次切换为写入模式，继续read
                buffer.clear();
            }
            // 强制刷盘
            destChannel.force(true);

            srcChannel.close();
            destChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
