/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Domain.Car;
import Util.HU;
import java.util.List;

/**
 *
 * @author chanelle
 */
public class ServicesDao extends GenericDao<Car>{
   public List<Car>view(String q) {
        return HU.getSessionFactory().openSession().createQuery(q).list();
    } 
}
