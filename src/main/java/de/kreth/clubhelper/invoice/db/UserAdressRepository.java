package de.kreth.clubhelper.invoice.db;

import org.springframework.data.repository.CrudRepository;

import de.kreth.clubhelper.invoice.data.User;
import de.kreth.clubhelper.invoice.data.UserAdress;

public interface UserAdressRepository extends CrudRepository<UserAdress, Integer> {

    UserAdress findByUser(User user);
}
