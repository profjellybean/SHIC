package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "register")
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER)
    @Column(name = "bills")
    private Set<Bill> bills;

    @Column(name = "monthlyPayment")
    private double monthlyPayments;

    @Column(name = "monthlyBudget")
    private double monthlyBudget;

    public Register() {

    }

    public Register(Long id, Set<Bill> bills, double monthlyPayments, double monthlyBudget) {
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

    public Set<Bill> getBills() {
        return bills;
    }

    public void setBills(Set<Bill> bills) {
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

    @Override
    public String toString() {
        return "Register{" +
            "id=" + id +
            ", bills=" + bills +
            ", monthlyPayments=" + monthlyPayments +
            ", monthlyBudget=" + monthlyBudget +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Register register = (Register) o;
        return Double.compare(register.monthlyPayments, monthlyPayments) == 0
            && Double.compare(register.monthlyBudget, monthlyBudget) == 0
            && Objects.equals(id, register.id)
            && Objects.equals(bills, register.bills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bills, monthlyPayments, monthlyBudget);
    }

    public static final class RegisterBuilder {
        private Long id;
        private Set<Bill> bills;
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

        public RegisterBuilder withBills(Set<Bill> bills) {
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
