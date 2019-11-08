package wallet.server.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.server.grpc.Wallet.WalletRequest;
import com.server.grpc.Wallet.WalletResponse;

import io.grpc.stub.StreamObserver;
import wallet.server.dao.WalletRepository;
import wallet.server.entity.Currency;
import wallet.server.entity.Users;
import wallet.server.entity.Wallets;

@RunWith(SpringRunner.class)
public class WalletServiceTest {

	@Autowired
	private WalletService walletService;

	@MockBean
	private WalletRepository walletRepository;

	@TestConfiguration
	static class WalletServiceTestContextConfiguration {

		@Bean
		public WalletService passportService() {

			return new WalletService();

		}

	}

	@Test
	public void whenBalanceIsPositive_itShouldReturnBalance() {

		WalletRequest request = WalletRequest.newBuilder().setAmount("100").setCurrency("USD").setUserID(1L).build();

		Wallets wallet = new Wallets();
		wallet.setBalance(new BigDecimal(200));

		Users user = new Users();
		user.setCurrency(Currency.valueOf("USD"));
		user.setUserId(1L);

		wallet.setUser(user);

		StreamObserver<WalletResponse> responseObserver = (StreamObserver<WalletResponse>) mock(StreamObserver.class);

		Mockito.when(walletRepository.getUserWalletsByCurrencyAndUserID(1L, Currency.valueOf("USD")))
				.thenReturn(wallet);

		walletService.balance(request, responseObserver);

		verify(responseObserver, times(1)).onNext(any(WalletResponse.class));

	}

	@Test
	public void whenBalanceIsPositiveAndCurrencyNotValid_itShouldThrowException() {

		WalletRequest request = WalletRequest.newBuilder().setAmount("100").setCurrency("SAR").setUserID(1L).build();

		StreamObserver<WalletResponse> responseObserver = (StreamObserver<WalletResponse>) mock(StreamObserver.class);

		walletService.balance(request, responseObserver);

		verify(responseObserver, times(1)).onError(any(Throwable.class));

	}

	@Test
	public void whenBalanceIsPositiveAndCurrencyIsEmpty_itShouldThrowException() {

		WalletRequest request = WalletRequest.newBuilder().setAmount("100").setCurrency("").setUserID(1L).build();

		StreamObserver<WalletResponse> responseObserver = (StreamObserver<WalletResponse>) mock(StreamObserver.class);

		walletService.balance(request, responseObserver);

		verify(responseObserver, times(1)).onError(any(Throwable.class));

	}

	@Test
	public void whenBalanceIsPositiveAndUserDoesnotExists_itShouldThrowException() {

		WalletRequest request = WalletRequest.newBuilder().setAmount("100").setCurrency("USD").setUserID(1L).build();

		StreamObserver<WalletResponse> responseObserver = (StreamObserver<WalletResponse>) mock(StreamObserver.class);

		Mockito.when(walletRepository.getUserWalletsByCurrencyAndUserID(1L, Currency.valueOf("USD"))).thenReturn(null);

		walletService.balance(request, responseObserver);

		verify(responseObserver, times(1)).onError(any(Throwable.class));

	}

	@Test
	public void whenBalanceIsPositive_itShouldWithdrawAmountGiven() {

		WalletRequest request = WalletRequest.newBuilder().setAmount("50").setCurrency("USD").setUserID(1L).build();

		Wallets wallet = new Wallets();
		wallet.setBalance(new BigDecimal(200));

		Users user = new Users();
		user.setCurrency(Currency.valueOf("USD"));
		user.setUserId(1L);

		wallet.setUser(user);

		StreamObserver<WalletResponse> responseObserver = (StreamObserver<WalletResponse>) mock(StreamObserver.class);

		Mockito.when(walletRepository.getUserWalletsByCurrencyAndUserID(1L, Currency.valueOf("USD")))
				.thenReturn(wallet);

		walletService.withdraw(request, responseObserver);

		verify(responseObserver, times(1)).onNext(any(WalletResponse.class));

	}
	
	@Test
	public void whenBalanceIsPositiveAndAmountIsZero_itShouldThrowException() {

		WalletRequest request = WalletRequest.newBuilder().setAmount("0").setCurrency("USD").setUserID(1L).build();

		Wallets wallet = new Wallets();
		wallet.setBalance(new BigDecimal(200));

		Users user = new Users();
		user.setCurrency(Currency.valueOf("USD"));
		user.setUserId(1L);

		wallet.setUser(user);

		StreamObserver<WalletResponse> responseObserver = (StreamObserver<WalletResponse>) mock(StreamObserver.class);

		Mockito.when(walletRepository.getUserWalletsByCurrencyAndUserID(1L, Currency.valueOf("USD")))
				.thenReturn(wallet);

		walletService.withdraw(request, responseObserver);

		verify(responseObserver, times(1)).onError(any(Throwable.class));

	}
	
	@Test
	public void whenBalanceIsZero_itShouldThrowException() {

		WalletRequest request = WalletRequest.newBuilder().setAmount("50").setCurrency("USD").setUserID(1L).build();

		Wallets wallet = new Wallets();
		wallet.setBalance(new BigDecimal(0));

		Users user = new Users();
		user.setCurrency(Currency.valueOf("USD"));
		user.setUserId(1L);

		wallet.setUser(user);

		StreamObserver<WalletResponse> responseObserver = (StreamObserver<WalletResponse>) mock(StreamObserver.class);

		Mockito.when(walletRepository.getUserWalletsByCurrencyAndUserID(1L, Currency.valueOf("USD")))
				.thenReturn(wallet);

		walletService.withdraw(request, responseObserver);

		verify(responseObserver, times(1)).onError(any(Throwable.class));

	}

}
