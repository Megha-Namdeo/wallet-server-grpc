package wallet.server.app;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import wallet.server.service.UserService;

public class ServerApplication {
	
public static void main(String[] args) throws  InterruptedException, IOException {
		

		System.out.println("Server is starting ");

		Server server = ServerBuilder.forPort(9090).addService(new UserService()).build();

		server.start();

		System.out.print("Server has been started at " + server.getPort());

		server.awaitTermination();

	}

}
