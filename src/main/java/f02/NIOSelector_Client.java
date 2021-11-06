package f02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOSelector_Client {

    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9496));

            socketChannel.configureBlocking(false);

            while(!socketChannel.finishConnect()) {
                System.out.println("处理别的事情等待连接完成。");
            }

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("Socket selector".getBytes());
            buffer.flip();

            socketChannel.write(buffer);
            socketChannel.shutdownOutput();
            socketChannel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
