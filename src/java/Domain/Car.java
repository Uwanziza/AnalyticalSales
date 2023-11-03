/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 *
 * @author chanelle
 */
@Entity
public class Car {

    @Id
    @GeneratedValue
    private Integer Id;
    private String Name, brand, PlateNo, Type, Photo, Status="Available";
    private Integer ManufacturingYear;
    private Long Cost, LeaseFee;
  


    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPlateNo() {
        return PlateNo;
    }

    public void setPlateNo(String PlateNo) {
        this.PlateNo = PlateNo;
    }

    public Integer getManufacturingYear() {
        return ManufacturingYear;
    }

    public void setManufacturingYear(Integer ManufacturingYear) {
        this.ManufacturingYear = ManufacturingYear;
    }


    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public Long getCost() {
        return Cost;
    }

    public void setCost(Long Cost) {
        this.Cost = Cost;
    }

    public Long getLeaseFee() {
        return LeaseFee;
    }

    public void setLeaseFee(Long LeaseFee) {
        this.LeaseFee = LeaseFee;
    }

 


}
