package de.kreth.clubhelper.invoice.ui;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LoginDataView extends VerticalLayout {

    private AccessToken token;

    public LoginDataView() {

	SecurityContext context = SecurityContextHolder.getContext();
	Authentication authentication = context.getAuthentication();
	KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) authentication.getPrincipal();
	token = principal.getKeycloakSecurityContext().getToken();

    }
}
