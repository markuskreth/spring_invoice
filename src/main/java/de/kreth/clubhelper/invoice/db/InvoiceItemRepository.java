package de.kreth.clubhelper.invoice.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.kreth.clubhelper.invoice.data.InvoiceItem;

public interface InvoiceItemRepository extends CrudRepository<InvoiceItem, Integer> {

    List<InvoiceItem> findByInvoiceIsNull();

}
