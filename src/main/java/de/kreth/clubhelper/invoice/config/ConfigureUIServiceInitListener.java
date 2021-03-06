package de.kreth.clubhelper.invoice.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

import de.kreth.clubhelper.invoice.ui.MainView;

//@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void serviceInit(ServiceInitEvent event) {
	event.getSource().addUIInitListener(uiEvent -> {
	    final UI ui = uiEvent.getUI();
	    ui.addBeforeEnterListener(this::beforeEnter); //
	});
    }

    /**
     * Reroutes the user if (s)he is not authorized to access the view.
     *
     * @param event before navigation event with event details
     */
    private void beforeEnter(BeforeEnterEvent event) {
	Class<?> navigationTarget = event.getNavigationTarget();

	if (isSecureAndNotAuthentificated(navigationTarget)) {
	    event.rerouteTo("");
	}
    }

    private boolean isSecureAndNotAuthentificated(Class<?> navigationTarget) {
	boolean userLoggedIn = isUserLoggedIn();
	return !MainView.class.equals(navigationTarget)
		&& !userLoggedIn;
    }

    boolean isUserLoggedIn() {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //
	return authentication != null //
		&& !(authentication instanceof AnonymousAuthenticationToken) //
		&& authentication.isAuthenticated(); //
    }
}
