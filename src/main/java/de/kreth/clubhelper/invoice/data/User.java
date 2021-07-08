package de.kreth.clubhelper.invoice.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.keycloak.representations.AccessToken;

@Entity
@Table(name = "USER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User extends BaseEntity {

    private static final long serialVersionUID = -2458030336216327580L;
    private String principalId;
    private String givenName;
    private String familyName;
    private String email;

    @Column(name = "PRINCIPAL_ID", nullable = false, length = 40, updatable = false, insertable = true, unique = true)
    public String getPrincipalId() {
	return principalId;
    }

    public void setPrincipal(AccessToken token) {
	if (this.principalId != null) {
	    if (!principalId.contentEquals(token.getSubject())) {
		throw new IllegalArgumentException("Non updateable value principalId does not match: this.principalId="
			+ this.principalId + ", token.principalId=" + token.getSubject());
	    }
	}
	principalId = token.getSubject();
	givenName = token.getGivenName();
	familyName = token.getFamilyName();
	email = token.getEmail();
    }

    @Column(nullable = false, length = 50, updatable = false, insertable = true)
    public String getGivenName() {
	return givenName;
    }

    @Column(nullable = false, length = 50, updatable = false, insertable = true)
    public String getFamilyName() {
	return familyName;
    }

    @Column(nullable = false, length = 100, updatable = false, insertable = true)
    public String getEmail() {
	return email;
    }

}
