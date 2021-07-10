package de.kreth.clubhelper.invoice.ui;

import java.util.concurrent.atomic.AtomicBoolean;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import de.kreth.clubhelper.invoice.data.User;
import de.kreth.clubhelper.invoice.data.UserAdress;
import de.kreth.clubhelper.invoice.data.UserBank;
import de.kreth.clubhelper.invoice.db.UserAdressRepository;
import de.kreth.clubhelper.invoice.db.UserBankRepository;
import de.kreth.clubhelper.invoice.db.UserRepository;

@Route("user")
@PreserveOnRefresh
public class LoginDataView extends VerticalLayout
	implements HasValue.ValueChangeListener<ValueChangeEvent<?>> {

    private static final long serialVersionUID = 3429612339118427050L;
    private final AccessToken token;
    private final UserBankRepository bankRepository;
    private final UserAdressRepository adressRepository;
    private final Binder<UserBank> bankBinder;
    private final Binder<UserAdress> adressBinder;

    private final AtomicBoolean hasChanges = new AtomicBoolean();

    private TextField street;
    private TextField plz;
    private TextField city;
    private TextField adressAdd;
    private TextField bankName;
    private TextField iban;
    private TextField bic;
    private Button storeButton;

    public LoginDataView(@Autowired UserRepository userRepository,
	    @Autowired UserBankRepository bankRepository,
	    @Autowired UserAdressRepository adressRepository) {

	this.bankRepository = bankRepository;
	this.adressRepository = adressRepository;
	this.bankBinder = new Binder<>(UserBank.class);
	this.adressBinder = new Binder<>(UserAdress.class);

	SecurityContext context = SecurityContextHolder.getContext();
	Authentication authentication = context.getAuthentication();
	KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) authentication.getPrincipal();
	token = principal.getKeycloakSecurityContext().getToken();

	createUi();

	initBinder(userRepository);

	toggleEnableStates();

	bankBinder.addValueChangeListener(this);
	adressBinder.addValueChangeListener(this);

    }

    private void initBinder(UserRepository userRepository) {
	User user = userRepository.findByPrincipalId(token.getSubject());
	UserBank bank = bankRepository.findByUser(user);

	if (bank == null) {
	    bank = new UserBank();
	    bank.setUser(user);
	}

	UserAdress adress = adressRepository.findByUser(user);
	if (adress == null) {
	    adress = new UserAdress();
	    adress.setUser(user);
	}

	bankBinder.forField(bankName)
		.asRequired("Bankname muss angegeben werden.")
		.withValidator(new StringLengthValidator("Bankname muss mind. 2 Zeichen enthalten.", 2, 150))
		.bind(UserBank::getBankName, UserBank::setBankName);

	bankBinder.forField(iban)
		.asRequired("IBAN muss angegeben werden.")
		.withValidator(new StringLengthValidator("IBAN muss 20 Zeichen enthalten.", 20, 30))
		.bind(UserBank::getIban, UserBank::setIban);
	bankBinder.forField(bic)
		.bind(UserBank::getBic, UserBank::setBic);

	adressBinder.forField(street)
		.asRequired("Straße muss angegeben werden.")
		.withValidator(new StringLengthValidator("Straße muss mind. 2 Zeichen enthalten", 2, 150))
		.bind(UserAdress::getAdress1, UserAdress::setAdress1);
	adressBinder.bind(adressAdd, UserAdress::getAdress2, UserAdress::setAdress2);
	adressBinder.forField(plz)
		.asRequired("PLZ muss angegeben werden")
		.withValidator(new StringLengthValidator("PLZ muss mind. 5 Zeichen enthalten", 5, 45))
		.bind(UserAdress::getZip, UserAdress::setZip);
	adressBinder.forField(city)
		.asRequired()
		.withValidator(new StringLengthValidator("Ort muss mind. 2 Zeichen enthalten", 2, 150))
		.bind(UserAdress::getCity, UserAdress::setCity);

	bankBinder.setBean(bank);
	adressBinder.setBean(adress);
    }

    private void createUi() {
	H1 caption = new H1("Pflichtangaben für Abrechnungen");
	H2 subtitle = new H2("Anmeldedaten");
	Label name = new Label(token.getGivenName() + " " + token.getFamilyName());
	Label email = new Label(token.getEmail());

	add(caption, subtitle, name, email);

	street = new TextField();
	street.setLabel("Straße");
	adressAdd = new TextField();
	adressAdd.setLabel("Adresszusatz");

	plz = new TextField();
	plz.setLabel("PLZ");
	city = new TextField();
	city.setLabel("Stadt");

	H2 captionAdress = new H2("Adresse");
	add(captionAdress, street, adressAdd, new HorizontalLayout(plz, city));

	bankName = new TextField();
	bankName.setLabel("Bankname");
	iban = new TextField();
	iban.setLabel("IBAN");
	bic = new TextField();
	bic.setLabel("BIC");

	H2 captionBank = new H2("Bankverbindung");
	add(captionBank, bankName, iban, bic);

	Label adressStatus = new Label();
	Label bankStatus = new Label();
	adressBinder.setStatusLabel(adressStatus);
	bankBinder.setStatusLabel(bankStatus);
	add(adressStatus, bankStatus);

	storeButton = new Button("Speichern", this::storeValues);

	add(storeButton);
    }

    void storeValues(ClickEvent<Button> ev) {
	if (adressBinder.isValid()) {
	    adressRepository.save(adressBinder.getBean());

	}
	if (bankBinder.isValid()) {
	    bankRepository.save(bankBinder.getBean());
	}
	if (adressBinder.isValid() && bankBinder.isValid()) {
	    hasChanges.set(false);
	}
    }

    void toggleEnableStates() {
	boolean bankHasValidChanged = bankBinder.validate().isOk();
	boolean adressHasValidChanged = adressBinder.validate().isOk();
	boolean enabled = bankHasValidChanged && adressHasValidChanged && hasChanges.get();
	storeButton.setEnabled(enabled);
	storeButton.getElement().setProperty("title", getStoreTooltip(enabled));
    }

    private String getStoreTooltip(boolean enabled) {
	if (enabled) {
	    return "Speichern der Änderungen";
	} else {
	    return "Speichern ist nur möglich, wenn alle Pflichtfelder korrekt gefüllt sind.";
	}
    }

    @Override
    public void valueChanged(ValueChangeEvent<?> event) {
	Object value = event.getValue();
	Object oldValue = event.getOldValue();
	boolean changed = value != null && !value.equals(oldValue) || oldValue != null;
	hasChanges.set(hasChanges.get() || changed);
	toggleEnableStates();
    }
}
