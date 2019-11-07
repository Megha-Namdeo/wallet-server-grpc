package wallet.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import wallet.server.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>{
	
	

}
