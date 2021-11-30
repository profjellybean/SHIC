package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;


public interface RegisterService {
    /**
     * Confirm an outstanding payment of a bill
     *
     */
    Register confirmPayment(Long registerId, Long billId, Long userId);

    Register findOne(Long id);
}
