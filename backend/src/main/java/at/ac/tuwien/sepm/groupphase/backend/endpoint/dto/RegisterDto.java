package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;


import java.util.Objects;
import java.util.Set;

public class RegisterDto {

    private Long id;

    private Set<Bill> bills;

    private double monthlyPayments;

    private double monthlyBudget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleMessageDto)) {
            return false;
        }
        RegisterDto that = (RegisterDto) o;
        return Objects.equals(id, that.id)
            && Objects.equals(bills, that.bills)
            && Objects.equals(monthlyPayments, that.monthlyPayments)
            && Objects.equals(monthlyBudget, that.monthlyBudget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bills, monthlyPayments, monthlyBudget);
    }

    @Override
    public String toString() {
        return "RegisterDto{"
            + "id=" + id
            + ", bills=" + bills
            + ", monthlyPayment='" + monthlyPayments + '\''
            + ", monthlyBudget='" + monthlyBudget + '\''
            + '}';
    }

    public static final class RegisterDtoBuilder {
        private Long id;
        private Set<Bill> bills;
        private double monthlyPayments;
        private double monthlyBudget;

        private RegisterDtoBuilder() {
        }

        public static RegisterDtoBuilder aRegisterDto() {
            return new RegisterDtoBuilder();
        }

        public RegisterDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public RegisterDtoBuilder withBills(Set<Bill> bills) {
            this.bills = bills;
            return this;
        }

        public RegisterDtoBuilder withMonthlyPayment(double monthlyPayments) {
            this.monthlyPayments = monthlyPayments;
            return this;
        }

        public RegisterDtoBuilder withMonthlyBudget(double monthlyBudget) {
            this.monthlyBudget = monthlyBudget;
            return this;
        }

        public RegisterDto build() {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setId(id);
            registerDto.setBills(bills);
            registerDto.setMonthlyPayments(monthlyPayments);
            registerDto.setMonthlyBudget(monthlyBudget);
            return registerDto;
        }
    }
}
