package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BillService {

    /**
     * Find a bill by a given id.
     *
     * @return the requested bill
     */
    Bill findOne(Long id);

    /**
     * Find all bills.
     *
     * @return a list of all bills
     */
    List<Bill> findAll();

    /**
     * Creates a bill.
     *
     * @param bill the bill that will be added
     */
    Bill bill(Bill bill);
}
