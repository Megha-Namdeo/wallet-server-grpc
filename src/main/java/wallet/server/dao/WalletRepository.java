package wallet.server.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import wallet.server.entity.Currency;
import wallet.server.entity.Users;
import wallet.server.entity.Wallets;

@Transactional
public interface WalletRepository extends PagingAndSortingRepository<Wallets, Users> {

	List<Wallets> findAllByUser(Long userId);

	@Query("select w from Wallet w where w.user.userID =:userID and w.user.currency=:currency")
	Wallets getUserWalletsByCurrencyAndUserID(@Param("userID") Long userID,
			@Param("currency") Currency currency);

}
