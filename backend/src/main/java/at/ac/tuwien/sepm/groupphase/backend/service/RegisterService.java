package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

import java.time.LocalDate;


public interface RegisterService {
    /**
     * Confirm an outstanding payment of a bill.
     */
    Register confirmPayment(Long registerId, Long billId, String username);

    /**
     * Find a register by a given id.
     *
     * @return the requested register
     */
    Register findOne(Long id);

    /**
     * Adds up the sums of all Bills that were paid in the current month
     * for all bills payed by anyone in the users current group
     * returns 0.0 if no bills can be found.
     *
     * @return sum of bills
     * @throws NotFoundException if no register can be found for the user
     */
    Double billSumOfCurrentMonth(String userName);


    /**
     * calculates the sum of the bills for the group.
     *
     * @return sum of the bills for the group
     * @throws NotFoundException if no register can be found for the user
     */
    Double billGroupTotal(String userName);

    /**
     * calculates the sum of the bills for the user.
     *
     * @return sum of the bills for the user
     * @throws NotFoundException if no register can be found for the user
     */
    Double billUserTotal(String userName);


    /**
     * Edits Budget in Register of the Group in which the User currently is.
     *
     * @param newBudget the new budget of the group
     * @param userName  the budget will be edited in this users group
     * @return new Budget
     * @throws ValidationException if the newBudget is invalid
     * @throws NotFoundException   if the register of the group can not be found
     */
    Double editMonthlyBudget(String userName, Double newBudget);

    /**
     * Adds up the sums of all Bills that were paid in a specific month.
     *
     * @return sum
     */
    Double billSumOfMonthAndYear(String userName, LocalDate date);

    /**
     * Adds up the sums of all Bills that were paid in a specific year.
     *
     * @return sum
     */
    Double billSumOfYear(String userName, LocalDate date);

}
