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

@WebServlet(urlPatterns = "/customers/findId")
public class FindByIdCustomerController extends HttpServlet {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            CustomerDto customer = customerService.getByID(id);
            if (customer.equals(new CustomerDto())) {
                req.setAttribute("message", "Customer with such id does not find");
                req.getRequestDispatcher("/WEB-INF/jsp/customers/findCustomerById.jsp").forward(req, resp);
            } else {
                req.setAttribute("customer", customer);
                req.getRequestDispatcher("/WEB-INF/jsp/customers/findCustomerById.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("message", "You enter wrong format. Please, try again");
            req.getRequestDispatcher("/WEB-INF/jsp/customers/findCustomerById.jsp").forward(req, resp);
        }
    }
}
