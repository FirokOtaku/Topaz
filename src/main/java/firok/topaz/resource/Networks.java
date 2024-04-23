package firok.topaz.resource;

import firok.topaz.TopazExceptions;
import firok.topaz.math.Maths;

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

    /**
     * 检查给定端口是否可用于创建 TCP socket
     * @since 7.26.0
     * */
    public static boolean isFreePortForTcp(int port)
    {
        TopazExceptions.ParamValueOutOfRange.maybe(!Maths.isInRange(port, 0, 65535));
        try(var ignored = new ServerSocket(port))
        {
            return true;
        }
        catch (IOException any)
        {
            return false;
        }
    }

    /**
     * 在给定端口范围内寻找可用的 TCP socket 端口号.
     * 端口号参数需要在 0 到 65535 范围内.
     * @return 如果找到的可用的端口则返回正整数, 否则返回 -1
     * @since 7.26.0
     * */
    public static int findFreePortForTcp(int portMin, int portMax)
    {
        TopazExceptions.ParamValueOutOfRange.maybe(!Maths.isInRange(portMin, 0, 65535));
        TopazExceptions.ParamValueOutOfRange.maybe(!Maths.isInRange(portMax, 0, 65535));
        for(int port = portMin; port <= portMax; port++)
        {
            try(var ignored = new ServerSocket(port))
            {
                return port;
            }
            catch (IOException ignored) { }
        }
        return -1;
    }
}
