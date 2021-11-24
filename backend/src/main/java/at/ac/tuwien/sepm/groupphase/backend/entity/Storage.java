package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

@Entity
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    private LinkedBlockingQueue<Item> items;

}
