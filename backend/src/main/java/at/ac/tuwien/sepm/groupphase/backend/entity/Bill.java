package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "register_id")
    private Long registerId;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<ItemStorage> groceries;

    @Column(name = "notes")
    private String notes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "included_id")
    private Set<ApplicationUser> names;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "notPaid_id")
    private Set<ApplicationUser> notPaidNames;

    @Column(name = "price")
    private double sum;

    @Column(name = "sumPerPerson")
    private double sumPerPerson;

    @PastOrPresent
    @Column(name = "paid_on")
    private LocalDate date;

    public Bill() {

    }

    public Bill(Long id, Long registerId, Set<ItemStorage> groceries, String notes, Set<ApplicationUser> names,
                Set<ApplicationUser> notPaidNames, double sum, double sumPerPerson, LocalDate date) {
        this.id = id;
        this.registerId = registerId;
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

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
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
    public String toString() {
        return "Bill{"
            +
            "id=" + id
            +
            ", registerId=" + registerId
            +
            ", groceries=" + groceries
            +
            ", notes='" + notes + '\''
            +
            ", names=" + names
            +
            ", notPaidNames=" + notPaidNames
            +
            ", sum=" + sum
            +
            ", sumPerPerson=" + sumPerPerson
            +
            ", date=" + date
            +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bill bill = (Bill) o;
        return Double.compare(bill.sum, sum) == 0
            && Double.compare(bill.sumPerPerson, sumPerPerson) == 0
            && Objects.equals(id, bill.id)
            && Objects.equals(registerId, bill.registerId)
            && Objects.equals(groceries, bill.groceries)
            && Objects.equals(notes, bill.notes)
            && Objects.equals(names, bill.names)
            && Objects.equals(notPaidNames, bill.notPaidNames)
            && Objects.equals(date, bill.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registerId, groceries, notes, names, notPaidNames, sum, sumPerPerson, date);
    }

    public static final class BillBuilder {
        private Long id;
        private Long registerId;
        private Set<ItemStorage> groceries;
        private String notes;
        private Set<ApplicationUser> names;
        private Set<ApplicationUser> notPaidNames;
        private double sum;
        private double sumPerPerson;
        private LocalDate date;

        public BillBuilder() {
        }

        public static BillBuilder aBill() {
            return new BillBuilder();
        }

        public BillBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public BillBuilder withRegisterId(Long registerId) {
            this.registerId = registerId;
            return this;
        }

        public BillBuilder withGroceries(Set<ItemStorage> groceries) {
            this.groceries = groceries;
            return this;
        }

        public BillBuilder withNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public BillBuilder withNames(Set<ApplicationUser> names) {
            this.names = names;
            return this;
        }

        public BillBuilder withNotPaidNames(Set<ApplicationUser> notPaidNames) {
            this.notPaidNames = notPaidNames;
            return this;
        }

        public BillBuilder withSum(double sum) {
            this.sum = sum;
            return this;
        }

        public BillBuilder withSumPerPerson(double sumPerPerson) {
            this.sumPerPerson = sumPerPerson;
            return this;
        }

        public BillBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Bill build() {
            Bill bill = new Bill();
            bill.setId(this.id);
            bill.setRegisterId(this.registerId);
            bill.setGroceries(this.groceries);
            bill.setNotes(this.notes);
            bill.setNames(this.names);
            bill.setNotPaidNames(this.notPaidNames);
            bill.setSum(this.sum);
            bill.setSumPerPerson(this.sumPerPerson);
            bill.setDate(this.date);
            return bill;
        }
    }
}
