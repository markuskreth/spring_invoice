package de.kreth.clubhelper.invoice.data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "BANKING_CONNECTION")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "owner_type", discriminatorType = DiscriminatorType.STRING)
public class BankingConnection extends BaseEntity {

    private static final long serialVersionUID = -6168631092559375156L;
    @Column(nullable = false, length = 150)
    private String bankName;
    @Column(nullable = false, length = 150)
    private String iban;
    @Column(nullable = true, length = 150)
    private String bic;

    public String getBankName() {
	return bankName;
    }

    public void setBankName(String bankName) {
	this.bankName = bankName;
    }

    public String getIban() {
	return iban;
    }

    public void setIban(String iban) {
	this.iban = iban;
    }

    public String getBic() {
	return bic;
    }

    public void setBic(String bic) {
	this.bic = bic;
    }

    @Override
    public String toString() {
	return iban;
    }

    public boolean isValid() {
	return iban != null;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((iban == null) ? 0 : iban.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	BankingConnection other = (BankingConnection) obj;
	if (iban == null) {
	    if (other.iban != null)
		return false;
	} else if (!iban.equals(other.iban))
	    return false;
	return true;
    }

}
