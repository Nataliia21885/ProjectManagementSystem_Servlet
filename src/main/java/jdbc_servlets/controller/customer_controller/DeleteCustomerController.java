package jdbc_servlets.controller.customer_controller;

import jdbc_servlets.config.DatabaseManagerConnector;
import jdbc_servlets.config.PropertiesConfig;
import jdbc_servlets.model.dto.CustomerDto;
import jdbc_servlets.repository.CustomerRepository;
import jdbc_servlets.repository.ProjectRepository;
import jdbc_servlets.service.CustomerService;
import jdbc_servlets.service.ProjectService;
import jdbc_servlets.service.converter.CustomerConverter;
import jdbc_servlets.service.converter.ProjectConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@WebServlet(urlPatterns = "/customers/drop")
public class DeleteCustomerController extends HttpServlet {
    private CustomerService customerService;
    private ProjectService projectService;

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
        ProjectRepository projectRepository = new ProjectRepository(dbManager);
        ProjectConverter projectConverter = new ProjectConverter();
        projectService = new ProjectService(projectRepository, projectConverter);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        CustomerDto customer = customerService.getByID(id);
        if (customer.equals(new CustomerDto())){
            req.setAttribute("message", "Customer with such id does not find");
            req.getRequestDispatcher("/WEB-INF/jsp/customers/deleteCustomer.jsp").forward(req, resp);
        } else {
            req.setAttribute("customer", customer);
            customerService.delete(customer);
            req.setAttribute("message", "Customer with such id successfully deleted");
            req.getRequestDispatcher("/WEB-INF/jsp/customers/deleteCustomer.jsp").forward(req, resp);
        }
    }
}
