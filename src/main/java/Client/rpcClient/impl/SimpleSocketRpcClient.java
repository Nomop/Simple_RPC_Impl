package Client.rpcClient.impl;

import Client.rpcClient.RpcClient;
import Common.Message.RpcRequest;
import Common.Message.RpcResponse;
import Server.server.RpcServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Description 对IOClient进行规范化
 * @Author nomo
 * @Version 1.1
 * @Date 2024/8/19 19:51
 */
public class SimpleSocketRpcClient implements RpcClient {

    private String host;
    private int port;

    public SimpleSocketRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        try {
            Socket socket = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(request);
            oos.flush();

            RpcResponse response = (RpcResponse) ois.readObject();
            return response;

        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
}
