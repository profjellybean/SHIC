package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "register_id")
    private Long registerId;

    @OneToMany
    @JoinColumn(name = "item_id")
    private Map<Long, Item> groceries;

    @Column(name = "notes")
    private String notes;

    @OneToMany
    @JoinColumn(name = "applicationUser_id")
    private Map<Long, ApplicationUser> names;

    @OneToMany
    @JoinColumn(name = "applicationUser_id")
    private Map<Long, ApplicationUser> notPaidNames;

    @Column(name = "sum")
    private double sum;

    @Column(name = "sumPerPerson")
    private double sumPerPerson;

    @PastOrPresent
    @Column(name = "date")
    private LocalDate date;

    public Bill() {

    }

    public Bill(Long id, HashMap<Long, Item> groceries, String notes, HashMap<Long, ApplicationUser> names, double sum, LocalDate date) {
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

    public Map<Long, Item> getGroceries() {
        return groceries;
    }

    public void setGroceries(HashMap<Long, Item> groceries) {
        this.groceries = groceries;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Map<Long, ApplicationUser> getNames() {
        return names;
    }

    public void setNames(Map<Long, ApplicationUser> names) {
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

    public void setNotPaidNames(Map<Long, ApplicationUser> notPaidNames) { this.notPaidNames = notPaidNames; }

    public Map<Long, ApplicationUser> getNotPaidNames() {
        return notPaidNames;
    }
}
