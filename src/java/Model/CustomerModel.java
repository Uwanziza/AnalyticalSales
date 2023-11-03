/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Dao.CarDao;
import Dao.CustomerDao;
import Dao.LeaseDao;
import Dao.SaleDao;
import Dao.UserDao;
import Domain.Car;
import Domain.Customer;
import Domain.Lease;
import Domain.Sale;
import Domain.User;
import Uploader.FileUploader;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Chanelle
 */
@ManagedBean
@SessionScoped
public class CustomerModel {

    private User u = new User();
    private String unipassword = new String();
    private Customer custdetails = new Customer();
    CustomerDao custDao = new CustomerDao();
    private String id = new String();
    private String searchKey = new String();
    private Customer customer = new Customer();
    private CarDao carDao= new CarDao();
    private Car car = new Car();

    private Customer loadCust = new Customer();
    private List<Customer> custList = new CustomerDao().findAll(Customer.class);
    List<Customer> custList2 = new CustomerDao().view("from Customer a WHERE a.Id='" + loadName2() + "'");
    List<Lease> leaseList2 = new LeaseDao().view("from Lease a WHERE a.customer.Id='" + loadName2() + "'");
    List<Sale> saleList2 = new SaleDao().view("from Sale a WHERE a.customer.Id='" + loadName2() + "' AND a.status='SOLD'");


    List<Car> carSaleList = new CarDao().view("from Car a WHERE a.Type='Sale'");
    List<Car> carLeaseList = new CarDao().view("from Car a WHERE a.Type='Lease'");
    private List<User> userList = new UserDao().findAll(User.class);
    private Car cardeatails = new Car();

    private Lease lease = new Lease();
    private LeaseDao leaseDao = new LeaseDao();
    private Sale sale = new Sale();
    private SaleDao saleDao = new SaleDao();
    private Customer load = new Customer();
    private List<Car> carList = new CarDao().findAll(Car.class);
    Integer flag = 0;

    @PostConstruct
    public void init() {
        custList = new CustomerDao().findAll(Customer.class);
        loadName2();
        flag = 0;

    }

    public void loadName() {
        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session");
        load = x.getCustomer();
    }

    public Integer loadName2() {
        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session");
        loadCust = x.getCustomer();
        Integer IdNum = loadCust.getId();
        return IdNum;
    }
    public void returnCar(Sale s){
        s.setStatus("RETURNED");
        new SaleDao().update(s);
        saleList2 = new SaleDao().view("from Sale a WHERE a.customer.Id='" + loadName2() + "' AND a.status='SOLD'");
        
       car= carDao.findbyId(Car.class, s.getCar().getId());
       car.setStatus("Available");
       carDao.update(car);
       carSaleList = new CarDao().view("from Car a WHERE a.Type='Sale'");
        
            FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Car Marked as Returned!", null));
    }

    public void createCustomer() {
        new CustomerDao().save(customer);
        u.setPassword(u.getPassword());
        u.setAccess("Customer");
        u.setCustomer(customer);

        new UserDao().save(u);

        u = new User();
        customer = new Customer();

        custList = new CustomerDao().findAll(Customer.class);
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account Created!", null));
    }

    public void createLease() {

        if (lease.getLeaseDate().before(new Date()) || lease.getReturnDate().before(new Date())) {

            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "The date entered is in the past!", null));
        } else {
            try {
//
//                Duration dur = Duration.between(lease.getLeaseDate(), lease.getReturnDate());
//    String result = String.format("%d:%02d", dur.toHours(), dur.toMinutesPart());
//    System.out.println(result);
                
                Date date = lease.getLeaseDate();
                Date date2 = lease.getReturnDate();

                // Display a date in day, month, year format
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String today = formatter.format(date);
                String nextday = formatter.format(date2);

// Convert `String` to `Date`
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date dateBefore = sdf.parse(today);
                Date dateAfter = sdf.parse(nextday);

// Calculate the number of days between dates
                long timeDiff = Math.abs(dateAfter.getTime() - dateBefore.getTime());
                long hoursDiff = TimeUnit.HOURS.convert(timeDiff, TimeUnit.MILLISECONDS);
                long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
                System.out.println("The number of hours between dates: " + hoursDiff);

                User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session");
                lease.setCustomer(x.getCustomer());
                lease.setCar(cardeatails);
                lease.setLease_Days(daysDiff);
                lease.setTotalAmount(daysDiff*cardeatails.getLeaseFee());
                leaseDao.save(lease);
                leaseList2 = new LeaseDao().view("from Lease a WHERE a.customer.Id='" + loadName2() + "'");
                lease = new Lease();
                
                
                        carList.forEach((ca) -> {
            if (ca.getId()==cardeatails.getId()) {
                
             
                    cardeatails.setId(ca.getId());
             cardeatails.setBrand(ca.getBrand());
             cardeatails.setCost(ca.getCost());
             cardeatails.setManufacturingYear(ca.getManufacturingYear());
             cardeatails.setName(ca.getName());
             cardeatails.setPhoto(ca.getPhoto());
             cardeatails.setPlateNo(ca.getPlateNo());
             cardeatails.setStatus("Not Available");
             cardeatails.setType(ca.getType());
                    new CarDao().update(cardeatails);

            }

        });
                
                
                FacesContext ctx = FacesContext.getCurrentInstance();
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You've succesfully leased "+ cardeatails.getName() +" for "+ hoursDiff +" Hours and you will pay "+ cardeatails.getLeaseFee()*daysDiff +" FRW!", null));
                cardeatails = new Car();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void createSale() {

        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session");
        sale.setCustomer(x.getCustomer());
        sale.setCar(cardeatails);
        
                         carList.forEach((ca) -> {
            if (ca.getId()==cardeatails.getId()) {
                
             
                    cardeatails.setId(ca.getId());
             cardeatails.setBrand(ca.getBrand());
             cardeatails.setCost(ca.getCost());
             cardeatails.setManufacturingYear(ca.getManufacturingYear());
             cardeatails.setName(ca.getName());
             cardeatails.setPhoto(ca.getPhoto());
             cardeatails.setPlateNo(ca.getPlateNo());
             cardeatails.setStatus("Sold");
             cardeatails.setType(ca.getType());
                    new CarDao().update(cardeatails);

            }

        });
        
        saleDao.save(sale);
                saleList2 = new SaleDao().view("from Sale a WHERE a.customer.Id='" + loadName2() + "' AND a.status='SOLD'");

        sale = new Sale();
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You've succesfully Submitted a sale for "+ cardeatails.getName() +"!", null));
//        cardeatails = new Car();
        flag=1;
        
    }

    public void updateCustomer() {
        custDao.update(custdetails);

        u.setCustomer(custdetails);

        custdetails = new Customer();
        custList = custDao.findAll(Customer.class);
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account Updated!", null));
    }

    public String fetchItems(final Customer cust) {
        this.custdetails = cust;
        return "UpdateInfo.xhtml?faces-redirect=true";
    }

    public String fetchICarDeatails1(final Car car) {
        this.cardeatails = car;
        return "Lease.xhtml?faces-redirect=true";
    }

    public String fetchICarDeatails2(final Car car) {
        this.cardeatails = car;
        return "Buy.xhtml?faces-redirect=true";
    }

    public void deleteCustomer() {
        custDao.delete(this.custdetails);
        custList = custDao.findAll(Customer.class);
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer Deleted!", null));
    }

    public void fetchAndShow(final Customer cust) {
        this.custdetails = cust;
        RequestContext.getCurrentInstance().execute("PF('delete').show()");
    }

    public void clearCustomerDetails() {
        this.custdetails = null;
    }

    public List<Customer> view() {
        List<Customer> list = new CustomerDao().findAll(Customer.class);
        return list;
    }

    public void updateInfo() {

        userList.forEach((us) -> {
            if (us.getAccess().matches("Customer") && loadCust.getId().equals(us.getCustomer().getId())) {

                if (!customer.getOldPass().equals(us.getPassword())) {
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old Password is Incorrect!", null));

                } else if (!customer.getNewPass().equals(customer.getConfNewPass())) {
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password do not match!", null));

                } else {

                    u.setId(us.getId());
                    u.setAccess(us.getAccess());
                    u.setCustomer(us.getCustomer());
                    u.setUsername(us.getUsername());
                    u.setPassword(customer.getNewPass());
                    new UserDao().update(u);
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Changed!", null));

                }

            }

        });

    }

    
     public void createPDFLease() {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            Document document = new Document();
            Rectangle rect = new Rectangle(20, 20, 800, 800);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setBoxSize("art", rect);
            document.setPageSize(rect);
            if (!document.isOpen()) {
                document.open();
            }
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("\\Dashboard");
            path = path.substring(0, path.indexOf("\\build"));
            path = path + "\\web\\Dashboard\\logo.png";
            Image image = Image.getInstance(path);
            image.scaleAbsolute(50, 50);
            image.setAlignment(Element.ALIGN_LEFT);
            Paragraph title = new Paragraph();
            //BEGIN page
            title.add(image);
            title.add("\n Autmotive Leasing");
            title.add("\n P.O. Box 0939 KIGALI - RWANDA");
            title.add("\n Email: info@carlease.gov.rw");
            document.add(title);

            Font font0 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.UNDERLINE);
            Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
            Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Font font5 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC);
            Font font6 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC);
            Font font7 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC);
            Font font8 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE);

            document.add(new Paragraph("\n"));
            Paragraph para = new Paragraph(" Leased Cars List  ", font0);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(new Paragraph("\n"));
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);

            PdfPCell cell1 = new PdfPCell(new Phrase("#", font2));
            cell1.setBorder(Rectangle.BOX);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase("Name", font2));
            cell2.setBorder(Rectangle.BOX);
            table.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Phrase("Brand", font2));
            cell3.setBorder(Rectangle.BOX);
            table.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Phrase("Plate No", font2));
            cell4.setBorder(Rectangle.BOX);
            table.addCell(cell4);

            PdfPCell cell5 = new PdfPCell(new Phrase("Manufacturing Year ", font2));
            cell5.setBorder(Rectangle.BOX);
            table.addCell(cell5);
            
            PdfPCell cell6 = new PdfPCell(new Phrase("Customer ", font2));
            cell6.setBorder(Rectangle.BOX);
            table.addCell(cell6);
            
            PdfPCell cell7 = new PdfPCell(new Phrase("Lease Date ", font2));
            cell7.setBorder(Rectangle.BOX);
            table.addCell(cell7);
            
            PdfPCell cell8 = new PdfPCell(new Phrase("Return Date ", font2));
            cell8.setBorder(Rectangle.BOX);
            table.addCell(cell8);
            
            PdfPCell cell9 = new PdfPCell(new Phrase("Days ", font2));
            cell9.setBorder(Rectangle.BOX);
            table.addCell(cell9);

            PdfPCell pdfc1;
            PdfPCell pdfc2;
            PdfPCell pdfc3;
            PdfPCell pdfc4;
            PdfPCell pdfc5;
            PdfPCell pdfc6;
            PdfPCell pdfc7;
            PdfPCell pdfc8;
            PdfPCell pdfc9;

            int i = 1;

            for (Lease lease : leaseList2 = new LeaseDao().view("from Lease a WHERE a.customer.Id='" + loadName2() + "'")) {
                pdfc1 = new PdfPCell(new Phrase(lease.getId().toString(), font6));
                pdfc1.setBorder(Rectangle.BOX);
                table.addCell(pdfc1);

                pdfc2 = new PdfPCell(new Phrase(lease.getCar().getName(), font6));
                pdfc2.setBorder(Rectangle.BOX);
                table.addCell(pdfc2);

                pdfc3 = new PdfPCell(new Phrase(lease.getCar().getBrand(), font6));
                pdfc3.setBorder(Rectangle.BOX);
                table.addCell(pdfc3);

                pdfc4 = new PdfPCell(new Phrase(lease.getCar().getPlateNo(), font6));
                pdfc4.setBorder(Rectangle.BOX);
                table.addCell(pdfc4);

                pdfc5 = new PdfPCell(new Phrase(lease.getCar().getManufacturingYear().toString(), font6));
                pdfc5.setBorder(Rectangle.BOX);
                table.addCell(pdfc5);

                pdfc6 = new PdfPCell(new Phrase(lease.getCustomer().getFName() + " " + lease.getCustomer().getLName(), font6));
                pdfc6.setBorder(Rectangle.BOX);
                table.addCell(pdfc6);

                pdfc7 = new PdfPCell(new Phrase(lease.getLeaseDate().toString(), font6));
                pdfc7.setBorder(Rectangle.BOX);
                table.addCell(pdfc7);

                pdfc8 = new PdfPCell(new Phrase(lease.getReturnDate().toString(), font6));
                pdfc8.setBorder(Rectangle.BOX);
                table.addCell(pdfc8);

                pdfc9 = new PdfPCell(new Phrase(lease.getLease_Days().toString(), font6));
                pdfc9.setBorder(Rectangle.BOX);
                table.addCell(pdfc9);

            }
            document.add(table);
            Paragraph p = new Paragraph("\n\nPrinted On: " + new Date(), font1);
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);
            Paragraph ps = new Paragraph("\n Automotive Sales &amp; Leasing Analytic System ", font1);
            ps.setAlignment(Element.ALIGN_RIGHT);
            document.add(ps);
            document.close();
            String fileName = "Leased Cars Report";

            writePDFToResponse(context.getExternalContext(), baos, fileName);

            context.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createPDFSale() {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            Document document = new Document();
            Rectangle rect = new Rectangle(20, 20, 800, 800);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setBoxSize("art", rect);
            document.setPageSize(rect);
            if (!document.isOpen()) {
                document.open();
            }
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("\\Dashboard");
            path = path.substring(0, path.indexOf("\\build"));
            path = path + "\\web\\Dashboard\\logo.png";
            Image image = Image.getInstance(path);
            image.scaleAbsolute(50, 50);
            image.setAlignment(Element.ALIGN_LEFT);
            Paragraph title = new Paragraph();
            //BEGIN page
            title.add(image);
            title.add("\n Autmotive Leasing");
            title.add("\n P.O. Box 0939 KIGALI - RWANDA");
            title.add("\n Email: info@carlease.gov.rw");
            document.add(title);

            Font font0 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.UNDERLINE);
            Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
            Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Font font5 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC);
            Font font6 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC);
            Font font7 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC);
            Font font8 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE);

            document.add(new Paragraph("\n"));
            Paragraph para = new Paragraph(" Sold Cars List  ", font0);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(new Paragraph("\n"));
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            PdfPCell cell1 = new PdfPCell(new Phrase("#", font2));
            cell1.setBorder(Rectangle.BOX);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase("Name", font2));
            cell2.setBorder(Rectangle.BOX);
            table.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Phrase("Brand", font2));
            cell3.setBorder(Rectangle.BOX);
            table.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Phrase("Plate No", font2));
            cell4.setBorder(Rectangle.BOX);
            table.addCell(cell4);

            PdfPCell cell5 = new PdfPCell(new Phrase("Manufacturing Year ", font2));
            cell5.setBorder(Rectangle.BOX);
            table.addCell(cell5);
            
            PdfPCell cell6 = new PdfPCell(new Phrase("Customer ", font2));
            cell6.setBorder(Rectangle.BOX);
            table.addCell(cell6);
            
            PdfPCell cell7 = new PdfPCell(new Phrase("Cost ", font2));
            cell7.setBorder(Rectangle.BOX);
            table.addCell(cell7);


            PdfPCell pdfc1;
            PdfPCell pdfc2;
            PdfPCell pdfc3;
            PdfPCell pdfc4;
            PdfPCell pdfc5;
            PdfPCell pdfc6;
            PdfPCell pdfc7;
            PdfPCell pdfc8;
            PdfPCell pdfc9;

            int i = 1;

            for (Sale sale : saleList2 = new SaleDao().view("from Sale a WHERE a.customer.Id='" + loadName2() + "'")) {
                pdfc1 = new PdfPCell(new Phrase(sale.getId().toString(), font6));
                pdfc1.setBorder(Rectangle.BOX);
                table.addCell(pdfc1);

                pdfc2 = new PdfPCell(new Phrase(sale.getCar().getName(), font6));
                pdfc2.setBorder(Rectangle.BOX);
                table.addCell(pdfc2);

                pdfc3 = new PdfPCell(new Phrase(sale.getCar().getBrand(), font6));
                pdfc3.setBorder(Rectangle.BOX);
                table.addCell(pdfc3);

                pdfc4 = new PdfPCell(new Phrase(sale.getCar().getPlateNo(), font6));
                pdfc4.setBorder(Rectangle.BOX);
                table.addCell(pdfc4);

                pdfc5 = new PdfPCell(new Phrase(sale.getCar().getManufacturingYear().toString(), font6));
                pdfc5.setBorder(Rectangle.BOX);
                table.addCell(pdfc5);

                pdfc6 = new PdfPCell(new Phrase(sale.getCustomer().getFName() + " " + sale.getCustomer().getLName(), font6));
                pdfc6.setBorder(Rectangle.BOX);
                table.addCell(pdfc6);

                pdfc7 = new PdfPCell(new Phrase(sale.getCar().getCost().toString(), font6));
                pdfc7.setBorder(Rectangle.BOX);
                table.addCell(pdfc7);

  
            }
            document.add(table);
            Paragraph p = new Paragraph("\n\nPrinted On: " + new Date(), font1);
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);
            Paragraph ps = new Paragraph("\n Automotive Sales &amp; Leasing Analytic System ", font1);
            ps.setAlignment(Element.ALIGN_RIGHT);
            document.add(ps);
            document.close();
            String fileName = "Sold Cars Report";

            writePDFToResponse(context.getExternalContext(), baos, fileName);

            context.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
        private void writePDFToResponse(ExternalContext externalContext, ByteArrayOutputStream baos, String fileName) {
        try {
            externalContext.responseReset();
            externalContext.setResponseContentType("application/pdf");
            externalContext.setResponseHeader("Expires", "0");
            externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            externalContext.setResponseHeader("Pragma", "public");
            externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
            externalContext.setResponseContentLength(baos.size());
            OutputStream out = externalContext.getResponseOutputStream();
            baos.writeTo(out);
            externalContext.responseFlushBuffer();
        } catch (IOException e) {

        }
    }
    
    
    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }

    public String getUnipassword() {
        return unipassword;
    }

    public void setUnipassword(String unipassword) {
        this.unipassword = unipassword;
    }

    public Customer getCustdetails() {
        return custdetails;
    }

    public void setCustdetails(Customer custdetails) {
        this.custdetails = custdetails;
    }

    public CustomerDao getCustDao() {
        return custDao;
    }

    public void setCustDao(CustomerDao custDao) {
        this.custDao = custDao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getLoad() {
        return load;
    }

    public void setLoad(Customer load) {
        this.load = load;
    }

    public Customer getLoadCust() {
        return loadCust;
    }

    public void setLoadCust(Customer loadCust) {
        this.loadCust = loadCust;
    }

    public List<Customer> getCustList() {
        return custList;
    }

    public void setCustList(List<Customer> custList) {
        this.custList = custList;
    }

    public List<Customer> getCustList2() {
        return custList2;
    }

    public void setCustList2(List<Customer> custList2) {
        this.custList2 = custList2;
    }

  
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Car> getCarSaleList() {
        return carSaleList;
    }

    public void setCarSaleList(List<Car> carSaleList) {
        this.carSaleList = carSaleList;
    }

    public List<Car> getCarLeaseList() {
        return carLeaseList;
    }

    public void setCarLeaseList(List<Car> carLeaseList) {
        this.carLeaseList = carLeaseList;
    }

    public Car getCardeatails() {
        return cardeatails;
    }

    public void setCardeatails(Car cardeatails) {
        this.cardeatails = cardeatails;
    }

    public Lease getLease() {
        return lease;
    }

    public void setLease(Lease lease) {
        this.lease = lease;
    }

    public LeaseDao getLeaseDao() {
        return leaseDao;
    }

    public void setLeaseDao(LeaseDao leaseDao) {
        this.leaseDao = leaseDao;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public SaleDao getSaleDao() {
        return saleDao;
    }

    public void setSaleDao(SaleDao saleDao) {
        this.saleDao = saleDao;
    }

    public List<Lease> getLeaseList2() {
        return leaseList2;
    }

    public void setLeaseList2(List<Lease> leaseList2) {
        this.leaseList2 = leaseList2;
    }

    public List<Sale> getSaleList2() {
        return saleList2;
    }

    public void setSaleList2(List<Sale> saleList2) {
        this.saleList2 = saleList2;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public CarDao getCarDao() {
        return carDao;
    }

    public void setCarDao(CarDao carDao) {
        this.carDao = carDao;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    
    
}
