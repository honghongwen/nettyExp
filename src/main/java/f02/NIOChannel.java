package f02;

import sun.nio.ch.IOUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO中一个连接就是用一个Channel（通道）来表示。
 * 从更广泛的层面来说，一个通道可以表示一个底层的文件描述符，
 * 例如硬件设备、文件、网络连接等。
 * 然而，远远不止如此，除了可以对应到底层文件描述符，
 * Java NIO的通道还可以更加细化。例如，对应不同的网络传输协议类型，
 * 在Java中都有不同的NIO Channel（通道）实现。
 * <p>
 * NIO中channel使用，最重要的四种channel.
 * FileChannel、SocketChannel、ServerSocketChannel、DatagramChannel
 */
public class NIOChannel {

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream(NIOChannel.class.getResource("/hello.txt").getFile());
            // 从inputStream中获取通道
            FileChannel channel = fis.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int length = -1;
            StringBuilder sb = new StringBuilder();
            // 如果到while体内再判断是否是-1,就会多一次读取。引发bug！！！
            while ((length = channel.read(buffer)) != -1) {
                String s = new String(buffer.array());
                sb.append(s.trim());
            }
            System.out.println(sb.toString());
            channel.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
