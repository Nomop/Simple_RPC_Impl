package Server;

import Common.service.Impl.UserServiceImpl;
import Common.service.UserService;
import Server.provider.ServiceProvider;
import Server.server.RpcServer;
import Server.server.impl.NettyRPCRPCServer;
import Server.server.impl.SimpleRPCRPCServer;
import Server.server.impl.ThreadPoolRPCRPCServer;

public class TestServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1",9999);

        serviceProvider.provideServiceInterface(userService,true);

        RpcServer server = new NettyRPCRPCServer(serviceProvider);
        server.start(9999);
    }
}
