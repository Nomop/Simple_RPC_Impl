package Client;

import Common.Message.RpcRequest;
import Common.Message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Descriptio 底层与服务端通信功能类，发送Request，返回Response
 * @Author nomo
 * @Version 1.0
 * @Date 2024/8/18 14:50
 */
public class IOClient {
    public static RpcResponse sendRequest(String host, int port, RpcRequest request){
        try {
            Socket socket = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(request);
            oos.flush();

            RpcResponse response = (RpcResponse) ois.readObject();
            return response;
        }catch (IOException |ClassNotFoundException e){
            e.printStackTrace();
            return null;

        }
    }
}
