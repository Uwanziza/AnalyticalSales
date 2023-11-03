/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Domain.Customer;
import Util.HU;
import java.util.List;

/**
 *
 * @author chanelle
 */
public class CustomerDao extends GenericDao<Customer>{
        public List<Customer>view(String q) {
        return HU.getSessionFactory().openSession().createQuery(q).list();
    }
}
