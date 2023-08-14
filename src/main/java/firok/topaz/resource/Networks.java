package firok.topaz.resource;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * 一些与网络相关的工具方法
 * @since 5.10.0
 * @author Firok
 * */
public final class Networks
{
    private Networks() { }

    /**
     * 寻找一个可创建服务端 TCP socket 的端口号
     * @return 如果找到一个可用端口, 返回正整数. 若找不到可用端口, 则返回 -1
     * */
    public static int findFreePortForTCP()
    {
        try(var serverSocket = new ServerSocket(0))
        {
            return serverSocket.getLocalPort();
        }
        catch (IOException any)
        {
            return -1;
        }
    }
}
