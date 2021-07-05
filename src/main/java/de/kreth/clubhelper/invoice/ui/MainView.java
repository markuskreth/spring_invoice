package de.kreth.clubhelper.invoice.ui;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.kreth.clubhelper.invoice.data.User;
import de.kreth.clubhelper.invoice.db.UserRepository;

@Route
@PageTitle("Personenliste")
public class MainView extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private AccessToken token;
    private Label name;
    private Label email;
    private Label id;

    public MainView(@Autowired UserRepository userRepository) {
	SecurityContext context = SecurityContextHolder.getContext();
	Authentication authentication = context.getAuthentication();
	KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) authentication.getPrincipal();
	token = principal.getKeycloakSecurityContext().getToken();

	createUi();
	User user = userRepository.findByPrincipalId(token.getSubject());
	if (user == null) {
	    user = new User();
	    user.setPrincipal(token);
	    userRepository.save(user);
	}
    }

    private void createUi() {

	Button menuButton = new Button(VaadinIcon.MENU.create());
	menuButton.addClickListener(this::onMenuButtonClick);

	Button addButton = new Button(VaadinIcon.PLUS_CIRCLE_O.create());

	HorizontalLayout l = new HorizontalLayout(menuButton, new H1("Übungsleiter Abrechnung"), addButton);
	l.setAlignItems(Alignment.CENTER);
	add(l);

	TextField filter = new TextField("Filter des Vor- oder Nachnamens");
	filter.setPlaceholder("Filter nach Name...");
	filter.setClearButtonVisible(true);

	name = new Label(token.getGivenName() + " " + token.getFamilyName());
	email = new Label(token.getEmail());
	id = new Label(token.getSubject());

	add(filter, name, email, id);

    }

    public void onMenuButtonClick(ClickEvent<Button> event) {
	ContextMenu menu = new ContextMenu();
	menu.setTarget(event.getSource());
	menu.addItem("Einstellungen", this::onSettingsButtonClick);
	menu.addItem("Über", this::onAboutButtonClick);
	menu.setVisible(true);
    }

    public void onSettingsButtonClick(ClickEvent<MenuItem> event) {
	Dialog dlg = new Dialog();
	dlg.add(new H1("Einstellungen"));
	dlg.add(new Text("Einstellugen für diese App. Noch nicht implementiert."));
	dlg.open();
    }

    public void onAboutButtonClick(ClickEvent<MenuItem> event) {

	Dialog dlg = new Dialog();
	dlg.add(new H1("Personeneditor"));
	dlg.add(new Text(
		"Personeneditor ist eine App zur Erfassung und Änderung von Personen im Trampolin des MTV Groß-Buchholz."));
	dlg.open();
    }

}
