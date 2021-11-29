package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "item_id")
    private Set<Item> groceries;

    @Column(name = "notes")
    private String notes;

    @OneToMany
    @JoinColumn(name = "applicationUser_id")
    private Set<ApplicationUser> names;

    @OneToMany
    @JoinColumn(name = "applicationUser_id")
    private Set<ApplicationUser> notPaidNames;

    @Column(name = "sum")
    private double sum;

    @Column(name = "sumPerPerson")
    private double sumPerPerson;

    @PastOrPresent
    @Column(name = "date")
    private LocalDate date;

    public Bill() {

    }

    public Bill(Long id, Set<Item> groceries, String notes, Set<ApplicationUser> names, double sum, LocalDate date) {
        this.id = id;
        this.groceries = groceries;
        this.notes = notes;
        this.names = names;
        this.sum = sum;
        this.date = date;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Set<Item> getGroceries() {
        return groceries;
    }

    public void setGroceries(Set<Item> groceries) {
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

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setSumPerPerson(double sumPerPerson) {
        this.sumPerPerson = sumPerPerson;
    }

    public double getSumPerPerson() {
        return sum / names.size();
    }

    public Set<ApplicationUser> getNotPaidNames() {
        return notPaidNames;
    }

    public void setNotPaidNames(Set<ApplicationUser> notPaidNames) {
        this.notPaidNames = notPaidNames;
    }
}
