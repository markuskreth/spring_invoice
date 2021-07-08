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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.kreth.clubhelper.invoice.data.User;
import de.kreth.clubhelper.invoice.data.UserAdress;
import de.kreth.clubhelper.invoice.data.UserBank;
import de.kreth.clubhelper.invoice.db.UserAdressRepository;
import de.kreth.clubhelper.invoice.db.UserBankRepository;
import de.kreth.clubhelper.invoice.db.UserRepository;

@Route
@PageTitle("Personenliste")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    private static final long serialVersionUID = 1L;
    private AccessToken token;
    private Label name;
    private Label email;
    private Label id;
    private UserRepository userRepository;
    private UserBankRepository bankRepository;
    private UserAdressRepository adressRepository;

    public MainView(@Autowired UserRepository userRepository, @Autowired UserBankRepository userBankRepository,
	    @Autowired UserAdressRepository adressRepository) {
	this.userRepository = userRepository;
	this.bankRepository = userBankRepository;
	this.adressRepository = adressRepository;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

	SecurityContext context = SecurityContextHolder.getContext();
	Authentication authentication = context.getAuthentication();
	KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) authentication.getPrincipal();
	token = principal.getKeycloakSecurityContext().getToken();

	User user = userRepository.findByPrincipalId(token.getSubject());
	if (user == null) {
	    user = new User();
	    user.setPrincipal(token);
	    userRepository.save(user);
	}

	UserBank bank = bankRepository.findByUser(user);
	UserAdress adress = adressRepository.findByUser(user);

	if (isBankOrAdressInvalid(bank, adress)) {
	    event.getUI().navigate(LoginDataView.class);
	} else {
	    createUi();
	}
    }

    private boolean isBankOrAdressInvalid(UserBank bank, UserAdress adress) {
	return bank == null || adress == null || !bank.isValid() || !adress.isValid();
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
