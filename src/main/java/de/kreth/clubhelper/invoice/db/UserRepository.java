package de.kreth.clubhelper.invoice.db;

import org.springframework.data.repository.CrudRepository;

import de.kreth.clubhelper.invoice.data.User;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByPrincipalId(String principalId);
}
