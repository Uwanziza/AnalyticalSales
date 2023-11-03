/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author chanelle
 */
@Entity
public class Lease {

    @Id
    @GeneratedValue
    private Integer Id;
    @OneToOne
    private Customer customer;
    @OneToOne
    private Car car;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date LeaseDate = new Date();
        @Temporal(javax.persistence.TemporalType.DATE)
    private Date ReturnDate = new Date();
        private Long Lease_Days, TotalAmount;
    private String leaseTime, returnTime;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Date getLeaseDate() {
        return LeaseDate;
    }

    public void setLeaseDate(Date LeaseDate) {
        this.LeaseDate = LeaseDate;
    }

    public Date getReturnDate() {
        return ReturnDate;
    }

    public void setReturnDate(Date ReturnDate) {
        this.ReturnDate = ReturnDate;
    }

    public Long getLease_Days() {
        return Lease_Days;
    }

    public void setLease_Days(Long Lease_Days) {
        this.Lease_Days = Lease_Days;
    }

    public Long getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Long TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public String getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(String leaseTime) {
        this.leaseTime = leaseTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

   
}
