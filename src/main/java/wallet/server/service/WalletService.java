package wallet.server.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.grpc.Wallet.WalletRequest;
import com.server.grpc.Wallet.WalletResponse;
import com.server.grpc.WalletServiceGrpc.WalletServiceImplBase;

import io.grpc.stub.StreamObserver;
import wallet.server.dao.WalletRepository;
import wallet.server.entity.Currency;
import wallet.server.entity.Users;
import wallet.server.entity.Wallets;
import wallet.server.exception.AppInvalidArgumentException;

@Service
public class WalletService extends WalletServiceImplBase {

	@Autowired
	private WalletRepository walletRepository;

	@Override
	@Transactional
	public void balance(WalletRequest request, StreamObserver<WalletResponse> responseObserver) {

		try {

			Wallets userWallet = validateUser(request);

			String balance = userWallet.getBalance().toPlainString();

			responseObserver
					.onNext(WalletResponse.newBuilder().setResponsemessage(balance).setResponsecode(100).build());

		} catch (AppInvalidArgumentException e) {

			responseObserver.onError(new Throwable(e.getMessage()));

		}

	}

	@Override
	@Transactional
	public void withdraw(WalletRequest request, StreamObserver<WalletResponse> responseObserver) {

		try {
			Wallets userWallet = validateUser(request);

			validateCurrency(request);

			BigDecimal withdrawAmount = validateAmount(request, userWallet.getBalance());

			BigDecimal newAmount = userWallet.getBalance().subtract(withdrawAmount);

			userWallet.setBalance(newAmount);

			walletRepository.save(userWallet);

			responseObserver.onNext(WalletResponse.newBuilder().setResponsemessage(newAmount.toPlainString()).build());

		} catch (AppInvalidArgumentException e) {

			responseObserver.onError(new Throwable(e.getMessage()));

		}

	}

	@Override
	@Transactional
	public void deposit(WalletRequest request, StreamObserver<WalletResponse> responseObserver) {

		try {

			Wallets userWallet = validateUser(request);

			validateCurrency(request);

			BigDecimal depositAmount = new BigDecimal(request.getAmount());

			BigDecimal newAmount = userWallet.getBalance().add(depositAmount);

			userWallet.setBalance(newAmount);

			walletRepository.save(userWallet);

			responseObserver.onNext(WalletResponse.newBuilder().setResponsemessage(newAmount.toPlainString()).build());

		} catch (AppInvalidArgumentException e) {

			responseObserver.onError(new Throwable(e.getMessage()));

		}

	}

	private BigDecimal validateAmount(WalletRequest request, BigDecimal balance) throws AppInvalidArgumentException {

		if (request.getAmount().isEmpty()) {

			throw new AppInvalidArgumentException("Amount should be provided for the operation");

		}

		BigDecimal amount = new BigDecimal(request.getAmount());

		if (amount.compareTo(BigDecimal.ZERO) < 1) {

			throw new AppInvalidArgumentException("Amount should be greater than 0");

		}

		if (balance.compareTo(amount) < 0) {

			throw new AppInvalidArgumentException("Insufficient funds for Withdrawal Operation ");

		}

		return amount;

	}

	private void validateCurrency(WalletRequest request) throws AppInvalidArgumentException {

		if (request.getCurrency().isEmpty()) {

			throw new AppInvalidArgumentException("Currency should be provided for the operation ");

		}

		try {

			Currency.valueOf(request.getCurrency().toUpperCase());

		} catch (IllegalArgumentException e) {

			throw new AppInvalidArgumentException("Currency should be in 'USD/EUR/GBP' ");

		}
	}

	private Wallets validateUser(WalletRequest request) throws AppInvalidArgumentException {

		Wallets wallet = null;

		try {

			Currency currency = Currency.valueOf(request.getCurrency().toUpperCase());

			wallet = walletRepository.getUserWalletsByCurrencyAndUserID(request.getUserID(), currency);

		} catch (IllegalArgumentException e) {

			throw new AppInvalidArgumentException("Currency should be in 'USD/EUR/GBP' ");

		}

		if (wallet == null) {

			throw new AppInvalidArgumentException("User Doesnot exist");

		}

		return wallet;

	}

}
