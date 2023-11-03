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
import Dao.ServicesDao;
import Dao.UserDao;
import Domain.Customer;
import Domain.Car;
import Domain.Lease;
import Domain.Sale;
import Domain.User;
import Uploader.FileUploader;
import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.api.SendSmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
 * @author chanelle
 */
@ManagedBean
@SessionScoped
public class AdminModel {

    private User u = new User();
    private String unipassword = new String();
    private List<Customer> custList = new CustomerDao().findAll(Customer.class);
    private List<Lease> leaseList = new LeaseDao().findAll(Lease.class);
    private List<Sale> saleList = new SaleDao().findAll(Sale.class);
    private List<Car> carList = new CarDao().findAll(Car.class);
    CustomerDao customerDao = new CustomerDao();
    private Customer customer = new Customer();
    private Car car = new Car();
    CarDao carDao = new CarDao();
    List<Car> carSaleList = new CarDao().view("from Car a WHERE a.Type='Sale'");
    List<Car> carLeaseList = new CarDao().view("from Car a WHERE a.Type='Lease'");

    private static final String BASE_URL = "https://wpy6jq.api.infobip.com";
    private static final String API_KEY = "07c950b61fe0e181f00af26d21cb67b7-0634d9b4-0b3a-44d9-8cf9-aff60626f8b3";
    private static final String SENDER = "InfoSMS";
    private static final String RECIPIENT = "";
    private static final String MESSAGE_TEXT = "";

    private List<String> choosenImage = new ArrayList<>();
    String conf_Psd;

    @PostConstruct
    public void init() {
//loadName();
    }

//    All Brands
    List<Car> toyotaList = new CarDao().view("from Car c WHERE c.brand='Toyota'");
    List<Car> hondaList = new CarDao().view("from Car c WHERE c.brand='Honda'");
    List<Car> benzList = new CarDao().view("from Car c WHERE c.brand='Mercedes-Benz'");
    List<Car> bmwList = new CarDao().view("from Car c WHERE c.brand='BMW'");
    List<Car> nissanList = new CarDao().view("from Car c WHERE c.brand='Nissan'");
    List<Car> audiList = new CarDao().view("from Car c WHERE c.brand='Audi'");
    List<Car> mazdaList = new CarDao().view("from Car c WHERE c.brand='Mazda'");

    Integer toyota = toyotaList.size();
    Integer honda = hondaList.size();
    Integer benz = benzList.size();
    Integer bmw = bmwList.size();
    Integer nissan = nissanList.size();
    Integer audi = audiList.size();
    Integer mazda = mazdaList.size();

//    All Leases
    List<Car> toyotaLeaseList = new CarDao().view("from Lease c WHERE c.car.brand='Toyota'");
    List<Car> hondaLeaseList = new CarDao().view("from Lease c WHERE c.car.brand='Honda'");
    List<Car> benzLeaseList = new CarDao().view("from Lease c WHERE c.car.brand='Mercedes-Benz'");
    List<Car> bmwLeaseList = new CarDao().view("from Lease c WHERE c.car.brand='BMW'");
    List<Car> nissanLeaseList = new CarDao().view("from Lease c WHERE c.car.brand='Nissan'");
    List<Car> audiLeaseList = new CarDao().view("from Lease c WHERE c.car.brand='Audi'");
    List<Car> mazdaLeaseList = new CarDao().view("from Lease c WHERE c.car.brand='Mazda'");

    Integer toyotaLease = toyotaLeaseList.size();
    Integer hondaLease = hondaLeaseList.size();
    Integer benzLease = benzLeaseList.size();
    Integer bmwLease = bmwLeaseList.size();
    Integer nissanLease = nissanLeaseList.size();
    Integer audiLease = audiLeaseList.size();
    Integer mazdaLease = mazdaLeaseList.size();

    //    All Sales
    List<Car> toyotaSaleList = new CarDao().view("from Sale c WHERE c.car.brand='Toyota'");
    List<Car> hondaSaleList = new CarDao().view("from Sale c WHERE c.car.brand='Honda'");
    List<Car> benzSaleList = new CarDao().view("from Sale c WHERE c.car.brand='Mercedes-Benz'");
    List<Car> bmwSaleList = new CarDao().view("from Sale c WHERE c.car.brand='BMW'");
    List<Car> nissanSaleList = new CarDao().view("from Sale c WHERE c.car.brand='Nissan'");
    List<Car> audiSaleList = new CarDao().view("from Sale c WHERE c.car.brand='Audi'");
    List<Car> mazdaSaleList = new CarDao().view("from Sale c WHERE c.car.brand='Mazda'");

    Integer toyotaSale = toyotaSaleList.size();
    Integer hondaSale = hondaSaleList.size();
    Integer benzSale = benzSaleList.size();
    Integer bmwSale = bmwSaleList.size();
    Integer nissanSale = nissanSaleList.size();
    Integer audiSale = audiSaleList.size();
    Integer mazdaSale = mazdaSaleList.size();

    public List<Sale> viewSale() {
        List<Sale> list = new SaleDao().findAll(Sale.class);
        return list;
    }

    public List<Lease> viewLease() {
        List<Lease> list = new LeaseDao().findAll(Lease.class);
        return list;
    }

    public void createCustomer() throws Exception {

        customer.setEmail(u.getUsername());
        new CustomerDao().save(customer);
        u.setPassword(u.getPassword());
        u.setAccess("Customer");
        u.setCustomer(customer);

        new UserDao().save(u);

        ApiClient client = initApiClient();

        SendSmsApi sendSmsApi = new SendSmsApi(client);

        SmsTextualMessage smsMessage = new SmsTextualMessage()
                .from(SENDER)
                .addDestinationsItem(new SmsDestination().to(customer.getPhone()))
                .text("Hello, " + customer.getFName() + " " + customer.getLName() + " your account has been successfully created and these are your credentials\n\n Username : " + u.getUsername() + "\n\n Password : " + u.getPassword() + " \n\nCAR LEASING AND SELLING");

        SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest()
                .messages(Collections.singletonList(smsMessage));

        try {
            SmsResponse smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest);
            System.out.println("Response body: " + smsResponse);
        } catch (ApiException e) {
            System.out.println("HTTP status code: " + e.getCode());
            System.out.println("Response body: " + e.getResponseBody());
        }

//        patList = new CustomerDao().findAll(Customer.class);
        custList = new CustomerDao().findAll(Customer.class);
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account Created Successfully!\n You can login using credentials that we sent your phone " + customer.getPhone() + ".", null));

        u = new User();
        customer = new Customer();
    }

    private static ApiClient initApiClient() {
        ApiClient client = new ApiClient();

        client.setApiKeyPrefix("App");
        client.setApiKey(API_KEY);
        client.setBasePath(BASE_URL);

        return client;
    }

    public void Upload(FileUploadEvent event) {
        choosenImage.add(new FileUploader().Upload(event, "C:\\Users\\cuwanziza\\Downloads\\Automotive Sales(3)\\web\\Photos\\"));
    }

    public void createCar() {
        if (choosenImage.isEmpty()) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please Upload a Picture!", null));
        } else {
            for (String x : choosenImage) {
                car.setPhoto(x);
            }
            choosenImage.clear();
            new CarDao().save(car);

            car = new Car();
            carList = new CarDao().findAll(Car.class);
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Car Succesfully Added!", null));
        }
    }

    public void makeAvailable(Car car) {

        car.setStatus("Available");
        new CarDao().update(car);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Status Updated!"));

    }

    public void createPDFCars() {
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
            Paragraph para = new Paragraph(" Cars List  ", font0);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(new Paragraph("\n"));
            PdfPTable table = new PdfPTable(5);
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

            PdfPCell pdfc1;
            PdfPCell pdfc2;
            PdfPCell pdfc3;
            PdfPCell pdfc4;
            PdfPCell pdfc5;
//            PdfPCell pdfc6;

            int i = 1;

            for (Car car : carList = new CarDao().findAll(Car.class)) {
                pdfc1 = new PdfPCell(new Phrase(car.getId().toString(), font6));
                pdfc1.setBorder(Rectangle.BOX);
                table.addCell(pdfc1);

                pdfc2 = new PdfPCell(new Phrase(car.getName(), font6));
                pdfc2.setBorder(Rectangle.BOX);
                table.addCell(pdfc2);

                pdfc3 = new PdfPCell(new Phrase(car.getBrand(), font6));
                pdfc3.setBorder(Rectangle.BOX);
                table.addCell(pdfc3);

                pdfc4 = new PdfPCell(new Phrase(car.getPlateNo(), font6));
                pdfc4.setBorder(Rectangle.BOX);
                table.addCell(pdfc4);

                pdfc5 = new PdfPCell(new Phrase(car.getManufacturingYear().toString(), font6));
                pdfc5.setBorder(Rectangle.BOX);
                table.addCell(pdfc5);

            }
            document.add(table);
            Paragraph p = new Paragraph("\n\nPrinted On: " + new Date(), font1);
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);
            Paragraph ps = new Paragraph("\n Automotive Sales &amp; Leasing Analytic System ", font1);
            ps.setAlignment(Element.ALIGN_RIGHT);
            document.add(ps);
            document.close();
            String fileName = "Cars Report";

            writePDFToResponse(context.getExternalContext(), baos, fileName);

            context.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }

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

            for (Lease lease : leaseList = new LeaseDao().findAll(Lease.class)) {
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

            for (Sale sale : saleList = new SaleDao().findAll(Sale.class)) {
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

    public List<Customer> getCustList() {
        return custList;
    }

    public void setCustList(List<Customer> custList) {
        this.custList = custList;
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public CarDao getCarDao() {
        return carDao;
    }

    public void setCarDao(CarDao carDao) {
        this.carDao = carDao;
    }

    public List<String> getChoosenImage() {
        return choosenImage;
    }

    public void setChoosenImage(List<String> choosenImage) {
        this.choosenImage = choosenImage;
    }

    public String getConf_Psd() {
        return conf_Psd;
    }

    public void setConf_Psd(String conf_Psd) {
        this.conf_Psd = conf_Psd;
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

    public List<Car> getToyotaList() {
        return toyotaList;
    }

    public void setToyotaList(List<Car> toyotaList) {
        this.toyotaList = toyotaList;
    }

    public List<Car> getHondaList() {
        return hondaList;
    }

    public void setHondaList(List<Car> hondaList) {
        this.hondaList = hondaList;
    }

    public List<Car> getBenzList() {
        return benzList;
    }

    public void setBenzList(List<Car> benzList) {
        this.benzList = benzList;
    }

    public List<Car> getBmwList() {
        return bmwList;
    }

    public void setBmwList(List<Car> bmwList) {
        this.bmwList = bmwList;
    }

    public List<Car> getNissanList() {
        return nissanList;
    }

    public void setNissanList(List<Car> nissanList) {
        this.nissanList = nissanList;
    }

    public List<Car> getAudiList() {
        return audiList;
    }

    public void setAudiList(List<Car> audiList) {
        this.audiList = audiList;
    }

    public List<Car> getMazdaList() {
        return mazdaList;
    }

    public void setMazdaList(List<Car> mazdaList) {
        this.mazdaList = mazdaList;
    }

    public Integer getToyota() {
        return toyota;
    }

    public void setToyota(Integer toyota) {
        this.toyota = toyota;
    }

    public Integer getHonda() {
        return honda;
    }

    public void setHonda(Integer honda) {
        this.honda = honda;
    }

    public Integer getBenz() {
        return benz;
    }

    public void setBenz(Integer benz) {
        this.benz = benz;
    }

    public Integer getBmw() {
        return bmw;
    }

    public void setBmw(Integer bmw) {
        this.bmw = bmw;
    }

    public Integer getNissan() {
        return nissan;
    }

    public void setNissan(Integer nissan) {
        this.nissan = nissan;
    }

    public Integer getAudi() {
        return audi;
    }

    public void setAudi(Integer audi) {
        this.audi = audi;
    }

    public Integer getMazda() {
        return mazda;
    }

    public void setMazda(Integer mazda) {
        this.mazda = mazda;
    }

    public List<Car> getToyotaLeaseList() {
        return toyotaLeaseList;
    }

    public void setToyotaLeaseList(List<Car> toyotaLeaseList) {
        this.toyotaLeaseList = toyotaLeaseList;
    }

    public List<Car> getHondaLeaseList() {
        return hondaLeaseList;
    }

    public void setHondaLeaseList(List<Car> hondaLeaseList) {
        this.hondaLeaseList = hondaLeaseList;
    }

    public List<Car> getBenzLeaseList() {
        return benzLeaseList;
    }

    public void setBenzLeaseList(List<Car> benzLeaseList) {
        this.benzLeaseList = benzLeaseList;
    }

    public List<Car> getBmwLeaseList() {
        return bmwLeaseList;
    }

    public void setBmwLeaseList(List<Car> bmwLeaseList) {
        this.bmwLeaseList = bmwLeaseList;
    }

    public List<Car> getNissanLeaseList() {
        return nissanLeaseList;
    }

    public void setNissanLeaseList(List<Car> nissanLeaseList) {
        this.nissanLeaseList = nissanLeaseList;
    }

    public List<Car> getAudiLeaseList() {
        return audiLeaseList;
    }

    public void setAudiLeaseList(List<Car> audiLeaseList) {
        this.audiLeaseList = audiLeaseList;
    }

    public List<Car> getMazdaLeaseList() {
        return mazdaLeaseList;
    }

    public void setMazdaLeaseList(List<Car> mazdaLeaseList) {
        this.mazdaLeaseList = mazdaLeaseList;
    }

    public Integer getToyotaLease() {
        return toyotaLease;
    }

    public void setToyotaLease(Integer toyotaLease) {
        this.toyotaLease = toyotaLease;
    }

    public Integer getHondaLease() {
        return hondaLease;
    }

    public void setHondaLease(Integer hondaLease) {
        this.hondaLease = hondaLease;
    }

    public Integer getBenzLease() {
        return benzLease;
    }

    public void setBenzLease(Integer benzLease) {
        this.benzLease = benzLease;
    }

    public Integer getBmwLease() {
        return bmwLease;
    }

    public void setBmwLease(Integer bmwLease) {
        this.bmwLease = bmwLease;
    }

    public Integer getNissanLease() {
        return nissanLease;
    }

    public void setNissanLease(Integer nissanLease) {
        this.nissanLease = nissanLease;
    }

    public Integer getAudiLease() {
        return audiLease;
    }

    public void setAudiLease(Integer audiLease) {
        this.audiLease = audiLease;
    }

    public Integer getMazdaLease() {
        return mazdaLease;
    }

    public void setMazdaLease(Integer mazdaLease) {
        this.mazdaLease = mazdaLease;
    }

    public List<Car> getToyotaSaleList() {
        return toyotaSaleList;
    }

    public void setToyotaSaleList(List<Car> toyotaSaleList) {
        this.toyotaSaleList = toyotaSaleList;
    }

    public List<Car> getHondaSaleList() {
        return hondaSaleList;
    }

    public void setHondaSaleList(List<Car> hondaSaleList) {
        this.hondaSaleList = hondaSaleList;
    }

    public List<Car> getBenzSaleList() {
        return benzSaleList;
    }

    public void setBenzSaleList(List<Car> benzSaleList) {
        this.benzSaleList = benzSaleList;
    }

    public List<Car> getBmwSaleList() {
        return bmwSaleList;
    }

    public void setBmwSaleList(List<Car> bmwSaleList) {
        this.bmwSaleList = bmwSaleList;
    }

    public List<Car> getNissanSaleList() {
        return nissanSaleList;
    }

    public void setNissanSaleList(List<Car> nissanSaleList) {
        this.nissanSaleList = nissanSaleList;
    }

    public List<Car> getAudiSaleList() {
        return audiSaleList;
    }

    public void setAudiSaleList(List<Car> audiSaleList) {
        this.audiSaleList = audiSaleList;
    }

    public List<Car> getMazdaSaleList() {
        return mazdaSaleList;
    }

    public void setMazdaSaleList(List<Car> mazdaSaleList) {
        this.mazdaSaleList = mazdaSaleList;
    }

    public Integer getToyotaSale() {
        return toyotaSale;
    }

    public void setToyotaSale(Integer toyotaSale) {
        this.toyotaSale = toyotaSale;
    }

    public Integer getHondaSale() {
        return hondaSale;
    }

    public void setHondaSale(Integer hondaSale) {
        this.hondaSale = hondaSale;
    }

    public Integer getBenzSale() {
        return benzSale;
    }

    public void setBenzSale(Integer benzSale) {
        this.benzSale = benzSale;
    }

    public Integer getBmwSale() {
        return bmwSale;
    }

    public void setBmwSale(Integer bmwSale) {
        this.bmwSale = bmwSale;
    }

    public Integer getNissanSale() {
        return nissanSale;
    }

    public void setNissanSale(Integer nissanSale) {
        this.nissanSale = nissanSale;
    }

    public Integer getAudiSale() {
        return audiSale;
    }

    public void setAudiSale(Integer audiSale) {
        this.audiSale = audiSale;
    }

    public Integer getMazdaSale() {
        return mazdaSale;
    }

    public void setMazdaSale(Integer mazdaSale) {
        this.mazdaSale = mazdaSale;
    }

}
