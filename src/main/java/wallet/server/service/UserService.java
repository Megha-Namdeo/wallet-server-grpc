package wallet.server.service;


import com.server.grpc.User.APIResponse;
import com.server.grpc.User.Empty;
import com.server.grpc.User.LoginRequest;
import com.server.grpc.userGrpc.userImplBase;

import io.grpc.stub.StreamObserver;

public class UserService extends userImplBase {

	@Override
	public void login(LoginRequest request, StreamObserver<APIResponse> responseObserver) {

		String username = request.getUsername();
		String password = request.getPassword();
		
		APIResponse.Builder res = APIResponse.newBuilder();

		if (username.equals(password)) {
			
			res.setResponsecode(0).setResponsemessage("SUCCESS");

		} else {
			
			res.setResponsecode(100).setResponsemessage("INVALID");

		}
		
		responseObserver.onNext(res.build());
		responseObserver.onCompleted();
	}

	@Override
	public void logout(Empty request, StreamObserver<APIResponse> responseObserver) {
		// TODO Auto-generated method stub
		super.logout(request, responseObserver);
	}

}
