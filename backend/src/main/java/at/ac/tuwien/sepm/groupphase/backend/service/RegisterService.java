package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;


public interface RegisterService {
    /**
     * Confirm an outstanding payment of a bill
     *
     */
    Register confirmPayment(Register register, Bill bill, ApplicationUser user);

    Register findOne(Long id);
}
