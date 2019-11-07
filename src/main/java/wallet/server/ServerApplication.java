package wallet.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {

		System.out.println("Server is starting ");

		SpringApplication.run(ServerApplication.class, args);

	}

}
