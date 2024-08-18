package Server.server.impl;

import Server.provider.ServiceProvider;
import Server.server.RpcServer;
import Server.server.work.WorkThread;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
public class SimpleRPCRPCServer implements RpcServer {

    private ServiceProvider serviceProvider;

    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务端启动,开始监听");
            //没有连接，则保持堵塞
            while (true){
                Socket socket = serverSocket.accept();
                new Thread(new WorkThread(socket,serviceProvider)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }
}
