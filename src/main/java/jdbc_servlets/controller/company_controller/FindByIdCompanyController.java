package jdbc_servlets.controller.company_controller;

import jdbc_servlets.config.DatabaseManagerConnector;
import jdbc_servlets.config.PropertiesConfig;
import jdbc_servlets.model.dto.CompanyDto;
import jdbc_servlets.repository.CompanyRepository;
import jdbc_servlets.service.CompanyService;
import jdbc_servlets.service.converter.CompanyConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@WebServlet(urlPatterns = "/companies/findId")
public class FindByIdCompanyController extends HttpServlet {
    private CompanyService companyService;

    @Override
    public void init() throws ServletException {
        String dbPassword = System.getenv("dbpassword");
        String dbUserName = System.getenv("dbusername");
        PropertiesConfig propertiesConfig = new PropertiesConfig();
        Properties properties = propertiesConfig.loadProperties("application.properties");
        DatabaseManagerConnector dbManager = new DatabaseManagerConnector(properties, dbUserName, dbPassword);
        CompanyRepository companyRepository = new CompanyRepository(dbManager);
        CompanyConverter companyConverter = new CompanyConverter();
        companyService = new CompanyService(companyRepository, companyConverter);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            CompanyDto company = companyService.getByID(id);
            if (company.equals(new CompanyDto())) {
                req.setAttribute("message", "Company with such id does not find");
                req.getRequestDispatcher("/WEB-INF/jsp/companies/findCompanyById.jsp").forward(req, resp);
            } else {
                req.setAttribute("company", company);
                req.getRequestDispatcher("/WEB-INF/jsp/companies/findCompanyById.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("message", "You enter wrong format. Please, try again");
            req.getRequestDispatcher("/WEB-INF/jsp/companies/findCompanyById.jsp").forward(req, resp);
        }
    }
}
