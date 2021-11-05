package f02;

import java.nio.IntBuffer;

/**
 * NIO里的buffer使用方法，一共8中buffer，对应7个数据类型和内存映射类型。
 * ByteBuffer、CharBuffer、DoubleBuffer、FloatBuffer、IntBuffer、LongBuffer、ShortBuffer、MappedByteBuffer。
 */
public class NIOBuffer {

    public static void main(String[] args) {
        IntBuffer ib = IntBuffer.allocate(20);
        System.out.println(String.format("position:%s，limit:%s，capacity:%s", ib.position(), ib.limit(), ib.capacity()));
        for (int i = 0; i < 5; i++) {
            ib.put(i);
        }
        System.out.println(String.format("position:%s，limit:%s，capacity:%s", ib.position(), ib.limit(), ib.capacity()));

        // 翻转缓冲区，从写模式到读模式
        // 调用ib.clear()、ib.compact()【压缩】就可以重新改为写模式
        ib.flip();
        System.out.println(String.format("position:%s，limit:%s，capacity:%s", ib.position(), ib.limit(), ib.capacity()));

        for (int i = 0; i < 2; i++) {
            int n = ib.get();
            System.out.println(n);
        }
        System.out.println(String.format("position:%s，limit:%s，capacity:%s", ib.position(), ib.limit(), ib.capacity()));

        // 倒带，重新读
        ib.rewind();
        for (int i = 0; i < 2; i++) {
            int n = ib.get();
            System.out.println(n);
        }
        System.out.println(String.format("position:%s，limit:%s，capacity:%s", ib.position(), ib.limit(), ib.capacity()));

        // 倒带 + mark标记buffer，之后再次reset从mark的位置开始读
        ib.rewind();
        for (int i = 0; i < 5; i++) {
            if (i == 2) ib.mark();
            System.out.println(ib.get());
        }
        System.out.println(String.format("position:%s，limit:%s，capacity:%s", ib.position(), ib.limit(), ib.capacity()));
        ib.reset();
        for (int i = 0; i < 3; i++) {
            int i1 = ib.get();
            System.out.println(i1);
        }
        System.out.println(String.format("position:%s，limit:%s，capacity:%s", ib.position(), ib.limit(), ib.capacity()));
    }

}
