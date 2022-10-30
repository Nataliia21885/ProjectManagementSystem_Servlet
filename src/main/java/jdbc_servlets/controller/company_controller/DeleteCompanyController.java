package jdbc_servlets.controller.company_controller;

import jdbc_servlets.config.DatabaseManagerConnector;
import jdbc_servlets.config.PropertiesConfig;
import jdbc_servlets.model.dto.CompanyDto;
import jdbc_servlets.repository.CompanyRepository;
import jdbc_servlets.repository.ProjectRepository;
import jdbc_servlets.service.CompanyService;
import jdbc_servlets.service.ProjectService;
import jdbc_servlets.service.converter.CompanyConverter;
import jdbc_servlets.service.converter.ProjectConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@WebServlet(urlPatterns = "/companies/drop")
public class DeleteCompanyController extends HttpServlet {
    private CompanyService companyService;
    private ProjectService projectService;

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
        ProjectRepository projectRepository = new ProjectRepository(dbManager);
        ProjectConverter projectConverter = new ProjectConverter();
        projectService = new ProjectService(projectRepository, projectConverter);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        CompanyDto company = companyService.getByID(id);
        if (company.equals(new CompanyDto())){
            req.setAttribute("message", "Company with such id does not find");
            req.getRequestDispatcher("/WEB-INF/jsp/companies/deleteCompany.jsp").forward(req, resp);
        } else {
            req.setAttribute("company", company);
            req.setAttribute("message", "Company with such id successfully deleted");
            companyService.delete(company);
            req.getRequestDispatcher("/WEB-INF/jsp/companies/deleteCompany.jsp").forward(req, resp);
        }
    }
}
