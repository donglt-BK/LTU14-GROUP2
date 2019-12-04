package com.bk.olympia.model.entity;

import javax.persistence.*;

/**
 * @author tmduc
 * @package com.bk.olympia.model.entity
 * @created 12/4/2019 3:55 PM
 */
@Entity
@Table(name = "room_topic")
public class RoomTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id")
    private Topic topic;
    private boolean canBeChosen;

    public RoomTopic() {
    }

    public RoomTopic(Room room, Topic topic, boolean canBeChosen) {
        this.room = room;
        this.topic = topic;
        this.canBeChosen = canBeChosen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public boolean isCanBeChosen() {
        return canBeChosen;
    }

    public void setCanBeChosen(boolean canBeChosen) {
        this.canBeChosen = canBeChosen;
    }
}
