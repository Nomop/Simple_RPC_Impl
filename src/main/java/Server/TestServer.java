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

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);

        //RpcServer server = new SimpleRPCRPCServer(serviceProvider);
        //RpcServer server = new ThreadPoolRPCRPCServer(serviceProvider);
        RpcServer server = new NettyRPCRPCServer(serviceProvider);
        server.start(9999);
    }
}
