package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RegisterDto;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "register")
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @Column(name = "bills")
    private Map<Long, Bill> bills;

    @Column(name = "monthlyPayment")
    private double monthlyPayments;

    @Column(name = "monthlyBudget")
    private double monthlyBudget;

    public Register() {

    }

    public Register(Long id, Map<Long, Bill> bills, double monthlyPayments, double monthlyBudget) {
        this.id = id;
        this.bills = bills;
        this.monthlyPayments = monthlyPayments;
        this.monthlyBudget = monthlyBudget;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Map<Long, Bill> getBills() {
        return bills;
    }

    public void setBills(Map<Long, Bill> bills) {
        this.bills = bills;
    }

    public double getMonthlyPayments() {
        return monthlyPayments;
    }

    public void setMonthlyPayments(double monthlyPayments) {
        this.monthlyPayments = monthlyPayments;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public static final class RegisterBuilder {
        private Long id;
        private Map<Long, Bill> bills;
        private double monthlyPayments;
        private double monthlyBudget;

        private RegisterBuilder() {
        }

        public static RegisterBuilder aRegister() {
            return new RegisterBuilder();
        }

        public RegisterBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public RegisterBuilder withBills(Map<Long, Bill> bills) {
            this.bills = bills;
            return this;
        }

        public RegisterBuilder withMonthlyPayment(double monthlyPayments) {
            this.monthlyPayments = monthlyPayments;
            return this;
        }

        public RegisterBuilder withMonthlyBudget(double monthlyBudget) {
            this.monthlyBudget = monthlyBudget;
            return this;
        }

        public Register build() {
            Register register = new Register();
            register.setId(id);
            register.setBills(bills);
            register.setMonthlyPayments(monthlyPayments);
            register.setMonthlyBudget(monthlyBudget);
            return register;
        }
    }
}
