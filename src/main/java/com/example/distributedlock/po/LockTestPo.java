package com.example.distributedlock.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "LockTest")
@Table(name = "Lock_Test")
public class LockTestPo {
    private Long id;
    private Long total;
    private Long booked;

    @Id
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "total")
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Column(name = "booked")
    public Long getBooked() {
        return booked;
    }

    public void setBooked(Long booked) {
        this.booked = booked;
    }
}
