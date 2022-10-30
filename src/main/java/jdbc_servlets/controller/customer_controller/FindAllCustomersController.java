package jdbc_servlets.controller.customer_controller;

import jdbc_servlets.config.DatabaseManagerConnector;
import jdbc_servlets.config.PropertiesConfig;
import jdbc_servlets.model.dto.CompanyDto;
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
import java.util.List;
import java.util.Properties;

@WebServlet(urlPatterns = "/customers/all")
public class FindAllCustomersController extends HttpServlet {
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
        List<CustomerDto> customers = customerService.findAll();
        req.setAttribute("customers", customers);
        req.getRequestDispatcher("/WEB-INF/jsp/customers/findAllCustomers.jsp").forward(req, resp);
    }
}
