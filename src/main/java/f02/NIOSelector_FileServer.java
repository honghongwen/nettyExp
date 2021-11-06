package f02;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 文件的传输，服务端
 */
public class NIOSelector_FileServer {

    public static void main(String[] args) {

        Map<SelectableChannel, Client> clientMap = new HashMap<>();
        try {
            Selector selector = Selector.open();

            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress("127.0.0.1", 9798));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (selector.select() > 0) {
                Iterator<SelectionKey> keyIt = selector.selectedKeys().iterator();

                while (keyIt.hasNext()) {
                    SelectionKey key = keyIt.next();
                    if (key.isAcceptable()) {
                        SocketChannel channel = serverChannel.accept();
                        channel.configureBlocking(false);
                        serverChannel.register(selector, SelectionKey.OP_READ);

                        Client client = new Client();
                        client.remoteAddress = (InetSocketAddress) channel.getRemoteAddress();
                        clientMap.put(channel, client);
                    }

                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        Client client = clientMap.get(key.channel());
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int length = 0;
                        while ((length = channel.read(buffer)) > 0) {
                            buffer.flip();

                            if (client.fileName == null) {

                                Charset charset = Charset.forName("UTF-8");
                                String fileName = charset.decode(buffer).toString();
                                File directory = new File(NIOSelector_FileServer.class.getResource("/nihao.txt").getFile());

                                if (!directory.exists()) {
                                    directory.mkdir();
                                }

                                client.fileName = fileName;
                                String fullName = directory.getAbsolutePath() + File.separatorChar + fileName;
                                File file = new File(fullName);

                                FileChannel fileChannel = new FileOutputStream(file).getChannel();
                                client.outChannel = fileChannel;
                            }

                            if (client.fileLength == 0) {
                                long fileLength = buffer.getLong();
                                client.fileLength = fileLength;
                                client.startTime = System.currentTimeMillis();
                            } else {
                                client.outChannel.write(buffer);
                            }

                            if (length == -1) {
                                System.out.println("文件上传完毕");
                                key.cancel();
                            }
                        }
                    }
                    keyIt.remove();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static class Client {

        String fileName;

        long fileLength;

        long startTime;

        InetSocketAddress remoteAddress;

        FileChannel outChannel;

    }

}
