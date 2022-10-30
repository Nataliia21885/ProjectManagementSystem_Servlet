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

@WebServlet(urlPatterns = "/companies")
public class CreateCompanyController extends HttpServlet {
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
        String name = req.getParameter("name");
        String hrm = req.getParameter("hrm");
        req.setAttribute("name", name);
        req.setAttribute("hrm", hrm);
        req.getRequestDispatcher("/WEB-INF/jsp/companies/createCompany.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String hrm = req.getParameter("hrm");
        CompanyDto company = new CompanyDto(name, hrm);
        if(!company.getName().isBlank() || !company.getHrm().isBlank()){
            companyService.create(company);
            req.setAttribute("company", company);
            req.setAttribute("message", "Company successfully created");
            req.getRequestDispatcher("/WEB-INF/jsp/companies/createCompany.jsp").forward(req, resp);
        } else {
            req.setAttribute("message", "Company isn't created");
            req.getRequestDispatcher("/WEB-INF/jsp/companies/createCompany.jsp").forward(req, resp);
        }
    }
}
