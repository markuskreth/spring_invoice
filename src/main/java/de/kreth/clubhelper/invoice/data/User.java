package de.kreth.clubhelper.invoice.data;

import javax.persistence.Column;

import org.keycloak.representations.AccessToken;

public class User extends BaseEntity {

    private static final long serialVersionUID = -2458030336216327580L;
    transient private AccessToken token;

    @Column(name = "PRINCIPAL_ID", nullable = false, length = 40, updatable = false, insertable = true)
    public String getPrincipalId() {
	return token.getSubject();
    }

    public void setPrincipal(AccessToken token) {
	this.token = token;
    }

    @Column(nullable = false, length = 50, updatable = false, insertable = true)
    public String getGivenName() {
	return token.getGivenName();
    }

    @Column(nullable = false, length = 50, updatable = false, insertable = true)
    public String getFamilyName() {
	return token.getFamilyName();
    }

    @Column(nullable = false, length = 100, updatable = false, insertable = true)
    public String getEmail() {
	return token.getEmail();
    }

}
