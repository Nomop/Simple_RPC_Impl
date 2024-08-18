package Server;

import Common.service.Impl.UserServiceImpl;
import Common.service.UserService;
import Server.provider.ServiceProvider;
import Server.server.RpcServer;
import Server.server.impl.SimpleRPCRPCServer;

public class TestServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);

        RpcServer server = new SimpleRPCRPCServer(serviceProvider);
        server.start(9999);
    }
}
