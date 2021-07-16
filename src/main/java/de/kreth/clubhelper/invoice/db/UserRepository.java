package de.kreth.clubhelper.invoice.db;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import de.kreth.clubhelper.invoice.data.User;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByPrincipalId(String principalId);

    default User getLoggedInUser() {

	SecurityContext context = SecurityContextHolder.getContext();
	Authentication authentication = context.getAuthentication();
	KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) authentication.getPrincipal();
	AccessToken token = principal.getKeycloakSecurityContext().getToken();
	return findByPrincipalId(token.getSubject());
    }
}
