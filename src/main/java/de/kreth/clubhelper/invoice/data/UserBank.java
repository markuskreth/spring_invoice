package de.kreth.clubhelper.invoice.data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class UserBank extends BankingConnection {

    private static final long serialVersionUID = -7356424394007978241L;
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private User user;

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
//	if (user.getBank() == null) {
//	    user.setBank(this);
//	} else {
//	    if (user.getBank().equals(this) == false) {
//		throw new IllegalArgumentException(
//			"User already set, but other than this.");
//	    }
//	}
    }

}
