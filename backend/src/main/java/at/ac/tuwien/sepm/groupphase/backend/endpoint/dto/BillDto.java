package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class BillDto {

    private Long id;

    private Set<ItemStorage> groceries;

    private String notes;

    private Set<ApplicationUser> names;

    private Set<ApplicationUser> notPaidNames;

    private double sum;

    private double sumPerPerson;

    private LocalDate date;

    public BillDto() {

    }

    public BillDto(Long id, Set<ItemStorage> groceries, String notes, Set<ApplicationUser> names,
                   Set<ApplicationUser> notPaidNames, double sum, double sumPerPerson, LocalDate date) {
        this.id = id;
        this.groceries = groceries;
        this.notes = notes;
        this.names = names;
        this.notPaidNames = notPaidNames;
        this.sum = sum;
        this.sumPerPerson = sumPerPerson;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ItemStorage> getGroceries() {
        return groceries;
    }

    public void setGroceries(Set<ItemStorage> groceries) {
        this.groceries = groceries;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<ApplicationUser> getNames() {
        return names;
    }

    public void setNames(Set<ApplicationUser> names) {
        this.names = names;
    }

    public Set<ApplicationUser> getNotPaidNames() {
        return notPaidNames;
    }

    public void setNotPaidNames(Set<ApplicationUser> notPaidNames) {
        this.notPaidNames = notPaidNames;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getSumPerPerson() {
        return sumPerPerson;
    }

    public void setSumPerPerson(double sumPerPerson) {
        this.sumPerPerson = sumPerPerson;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleMessageDto)) {
            return false;
        }
        BillDto that = (BillDto) o;
        return Objects.equals(id, that.id)
            && Objects.equals(groceries, that.groceries)
            && Objects.equals(notes, that.notes)
            && Objects.equals(names, that.names)
            && Objects.equals(notPaidNames, that.notPaidNames)
            && Objects.equals(sum, that.sum)
            && Objects.equals(sumPerPerson, that.sumPerPerson)
            && Objects.equals(date, that.date);
    }


    public static class BillDtoBuilder {
        private Long id;
        private Set<ItemStorage> groceries;
        private String notes;
        private Set<ApplicationUser> names;
        private Set<ApplicationUser> notPaidNames;
        private double sum;
        private double sumPerPerson;
        private LocalDate date;

        private BillDtoBuilder() {
        }

        public static BillDtoBuilder anBillDto() {
            return new BillDtoBuilder();
        }

        public BillDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public BillDtoBuilder withGroceries(Set<ItemStorage> groceries) {
            this.groceries = groceries;
            return this;
        }

        public BillDtoBuilder withNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public BillDtoBuilder withNames(Set<ApplicationUser> names) {
            this.names = names;
            return this;
        }

        public BillDtoBuilder withNotPaidNames(Set<ApplicationUser> notPaidNames) {
            this.notPaidNames = notPaidNames;
            return this;
        }

        public BillDtoBuilder withSum() {
            return this;
        }

        public BillDtoBuilder withSumPerPerson(double sumPerPerson) {
            this.sumPerPerson = sumPerPerson;
            return this;
        }

        public BillDtoBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public BillDto build() {
            BillDto billDto = new BillDto();
            billDto.setId(id);
            billDto.setGroceries(groceries);
            billDto.setNotes(notes);
            billDto.setNames(names);
            billDto.setNotPaidNames(notPaidNames);
            billDto.setSum(sum);
            billDto.setSumPerPerson(sumPerPerson);
            billDto.setDate(date);
            return billDto;
        }
    }
}
