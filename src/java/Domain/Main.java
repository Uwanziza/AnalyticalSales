/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domain;

import Dao.UserDao;
import Util.HU;

/**
 *
 * @author chanelle
 */
public class Main {
  
    public static void main(String[] args) {
        HU.getSessionFactory().openSession();
        
         User user= new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setAccess("Admin"); 
        UserDao userDao= new UserDao();
        userDao.save(user);
    }
    
}


