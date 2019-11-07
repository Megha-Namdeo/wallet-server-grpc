package wallet.server.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.grpc.Wallet.WalletRequest;
import com.server.grpc.Wallet.WalletResponse;
import com.server.grpc.WalletServiceGrpc.WalletServiceImplBase;

import io.grpc.stub.StreamObserver;
import wallet.server.dao.WalletRepository;
import wallet.server.entity.Wallet;

@Service
public class WalletService extends WalletServiceImplBase {

	@Autowired
	private WalletRepository walletRepository;

	@Override
	@Transactional
	public void balance(WalletRequest request, StreamObserver<WalletResponse> responseObserver) {

		Wallet userWallet = walletRepository.findById(request.getUserID()).orElse(null);

		if (userWallet != null) {

			String balance = userWallet.getBalance().toPlainString();

			responseObserver
					.onNext(WalletResponse.newBuilder().setResponsemessage(balance).setResponsecode(100).build());

		} else {

			responseObserver.onError(new Throwable("User Wallet is Null"));
		}

	}

}
