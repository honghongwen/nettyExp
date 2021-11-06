package f02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 一个线程对应一个selector,一个selector可以注册多个channel,但是channel必须是selectableChannel子类。
 * selector中可以拿到具体的io事件和channel。
 */
public class NIOSelector {

    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            int i = serverSocketChannel.validOps();
            System.out.println(i);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 9496));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }

                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        int length = 0;
                        while ((length = socketChannel.read(buffer)) != -1) {
                            buffer.flip();
                            byte[] array = buffer.array();
                            System.out.println(new String(array).trim());
                            buffer.clear();
                        }
                        socketChannel.close();
                    }
                    iterator.remove();
                }
                serverSocketChannel.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
