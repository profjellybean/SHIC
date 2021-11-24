package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.concurrent.LinkedBlockingQueue;

@Entity
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private LinkedBlockingQueue<Item> items;

    public Storage(LinkedBlockingQueue<Item> items) {
        this.items = items;
    }

    public Storage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LinkedBlockingQueue<Item> getItems() {
        return items;
    }

    public void setItems(LinkedBlockingQueue<Item> items) {
        this.items = items;
    }
}
