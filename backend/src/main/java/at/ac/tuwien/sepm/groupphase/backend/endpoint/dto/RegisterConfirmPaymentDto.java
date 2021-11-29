package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;

import java.util.Objects;
import java.util.Set;

public class RegisterConfirmPaymentDto {
    private Long id;

    private Set<Bill> billSet;

    private Bill bill;

    private ApplicationUser user;

    private double monthlyPayments;

    private double monthlyBudget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Bill> getBillSet() {
        return billSet;
    }

    public void setBillSet(Set<Bill> billSet) {
        this.billSet = billSet;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
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
        RegisterConfirmPaymentDto that = (RegisterConfirmPaymentDto) o;
        return Objects.equals(id, that.id)
            && Objects.equals(billSet, that.billSet)
            && Objects.equals(bill, that.bill)
            && Objects.equals(user, that.user)
            && Objects.equals(monthlyPayments, that.monthlyPayments)
            && Objects.equals(monthlyBudget, that.monthlyBudget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, billSet, bill, monthlyPayments, monthlyBudget);
    }

    @Override
    public String toString() {
        return "SimpleMessageDto{"
            + "id=" + id
            + ", billSet=" + billSet
            + ", bill=" + bill
            + ", user=" + user
            + ", monthlyPayment='" + monthlyPayments + '\''
            + ", monthlyBudget='" + monthlyBudget + '\''
            + '}';
    }

    public static final class RegisterComfirmPaymentDtoBuilder {
        private Long id;
        private Set<Bill> billSet;
        private Bill bill;
        private ApplicationUser user;
        private double monthlyPayments;
        private double monthlyBudget;

        private RegisterComfirmPaymentDtoBuilder() {
        }

        public static RegisterComfirmPaymentDtoBuilder aRegisterDto() {
            return new RegisterComfirmPaymentDtoBuilder();
        }

        public RegisterComfirmPaymentDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public RegisterComfirmPaymentDtoBuilder withBillSet(Set<Bill> billSet) {
            this.billSet = billSet;
            return this;
        }

        public RegisterComfirmPaymentDtoBuilder withBill(Bill bill) {
            this.bill = bill;
            return this;
        }

        public RegisterComfirmPaymentDtoBuilder withUser(ApplicationUser user) {
            this.user = user;
            return this;
        }

        public RegisterComfirmPaymentDtoBuilder withMonthlyPayment(double monthlyPayments) {
            this.monthlyPayments = monthlyPayments;
            return this;
        }

        public RegisterComfirmPaymentDtoBuilder withMonthlyBudget(double monthlyBudget) {
            this.monthlyBudget = monthlyBudget;
            return this;
        }

        public RegisterConfirmPaymentDto build() {
            RegisterConfirmPaymentDto registerConfirmPaymentDto = new RegisterConfirmPaymentDto();
            registerConfirmPaymentDto.setId(id);
            registerConfirmPaymentDto.setBillSet(billSet);
            registerConfirmPaymentDto.setBill(bill);
            registerConfirmPaymentDto.setUser(user);
            registerConfirmPaymentDto.setMonthlyPayments(monthlyPayments);
            registerConfirmPaymentDto.setMonthlyBudget(monthlyBudget);
            return registerConfirmPaymentDto;
        }
    }
}

