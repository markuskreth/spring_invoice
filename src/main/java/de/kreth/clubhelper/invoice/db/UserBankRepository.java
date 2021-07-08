package de.kreth.clubhelper.invoice.db;

import org.springframework.data.repository.CrudRepository;

import de.kreth.clubhelper.invoice.data.User;
import de.kreth.clubhelper.invoice.data.UserBank;

public interface UserBankRepository extends CrudRepository<UserBank, Integer> {

    UserBank findByUser(User user);
}
