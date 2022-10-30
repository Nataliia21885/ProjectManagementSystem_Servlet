package jdbc_servlets.controller.customer_controller;

import jdbc_servlets.config.DatabaseManagerConnector;
import jdbc_servlets.config.PropertiesConfig;
import jdbc_servlets.model.dto.CustomerDto;
import jdbc_servlets.repository.CustomerRepository;
import jdbc_servlets.service.CustomerService;
import jdbc_servlets.service.converter.CustomerConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@WebServlet(urlPatterns = "/customers/upd")
public class UpdateCustomerController extends HttpServlet {
    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        String dbPassword = System.getenv("dbpassword");
        String dbUserName = System.getenv("dbusername");
        PropertiesConfig propertiesConfig = new PropertiesConfig();
        Properties properties = propertiesConfig.loadProperties("application.properties");
        DatabaseManagerConnector dbManager = new DatabaseManagerConnector(properties, dbUserName, dbPassword);
        CustomerRepository customerRepository = new CustomerRepository(dbManager);
        CustomerConverter customerConverter = new CustomerConverter();
        customerService = new CustomerService(customerRepository, customerConverter);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("name");
            String contact = req.getParameter("contact");
            CustomerDto findById = customerService.getByID(id);
            if (!name.isBlank() && !contact.isBlank() && findById.getId() != null ) {
                CustomerDto customer = new CustomerDto(id, name, contact);
                CustomerDto updatedCustomer = customerService.update(customer);
                req.setAttribute("updatedCustomer", updatedCustomer);
                req.setAttribute("message", "Customer is updated successfully");
                req.getRequestDispatcher("/WEB-INF/jsp/customers/updateCustomer.jsp").forward(req, resp);
            } else {
                req.setAttribute("message", "Customer is not updated (fields are empty or entering id does not exist)");
                req.getRequestDispatcher("/WEB-INF/jsp/customers/updateCustomer.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("message", "You enter wrong format. Please, try again");
            req.getRequestDispatcher("/WEB-INF/jsp/customers/updateCustomer.jsp").forward(req, resp);
        }
    }
}
