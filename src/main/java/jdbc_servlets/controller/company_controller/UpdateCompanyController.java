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

@WebServlet(urlPatterns = "/companies/upd")
public class UpdateCompanyController extends HttpServlet {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("name");
            String hrm = req.getParameter("hrm");
            CompanyDto findById = companyService.getByID(id);
            if (!name.isBlank() && !hrm.isBlank() && findById.getId() != null ) {
                    CompanyDto company = new CompanyDto(id, name, hrm);
                    CompanyDto updatedCompany = companyService.update(company);
                    req.setAttribute("updatedCompany", updatedCompany);
                    req.setAttribute("message", "Company is updated successfully");
                    req.getRequestDispatcher("/WEB-INF/jsp/companies/updateCompany.jsp").forward(req, resp);
            } else {
                req.setAttribute("message", "Company is not updated (fields are empty or entering id does not exist)");
                req.getRequestDispatcher("/WEB-INF/jsp/companies/updateCompany.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("message", "You enter wrong format. Please, try again");
            req.getRequestDispatcher("/WEB-INF/jsp/companies/updateCompany.jsp").forward(req, resp);
        }
    }
}
