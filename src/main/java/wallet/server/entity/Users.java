package wallet.server.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
public class Users {

	@Id
	@Column(name = "user_id", nullable = false)
	@Size(max = 20)
	private Long userId;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Currency currency;

	@OneToMany(mappedBy = "wallet")
	private List<Wallets> wallets = new ArrayList<>();

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public List<Wallets> getWallets() {
		return wallets;
	}

	public void setWallets(List<Wallets> wallets) {
		this.wallets = wallets;
	}

}
