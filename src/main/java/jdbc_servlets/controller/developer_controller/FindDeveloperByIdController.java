package jdbc_servlets.controller.developer_controller;

import jdbc_servlets.config.DatabaseManagerConnector;
import jdbc_servlets.config.PropertiesConfig;
import jdbc_servlets.model.dto.CompanyDto;
import jdbc_servlets.model.dto.DeveloperDto;
import jdbc_servlets.repository.DeveloperRepository;
import jdbc_servlets.service.DeveloperService;
import jdbc_servlets.service.converter.DeveloperConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@WebServlet(urlPatterns = "/developers/findId")
public class FindDeveloperByIdController extends HttpServlet {
    private DeveloperService developerService;

    @Override
    public void init() throws ServletException {
        String dbPassword = System.getenv("dbpassword");
        String dbUserName = System.getenv("dbusername");
        PropertiesConfig propertiesConfig = new PropertiesConfig();
        Properties properties = propertiesConfig.loadProperties("application.properties");
        DatabaseManagerConnector dbManager = new DatabaseManagerConnector(properties, dbUserName, dbPassword);
        DeveloperRepository developerRepository = new DeveloperRepository(dbManager);
        DeveloperConverter developerConverter = new DeveloperConverter();
        developerService = new DeveloperService(developerRepository, developerConverter);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            DeveloperDto developer = developerService.getByID(id);
            if (developer.equals(new DeveloperDto())) {
                req.setAttribute("message", "Developer with such id does not find");
                req.getRequestDispatcher("/WEB-INF/jsp/developers/findDeveloperById.jsp").forward(req, resp);
            } else {
                req.setAttribute("developer", developer);
                req.getRequestDispatcher("/WEB-INF/jsp/developers/findDeveloperById.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("message", "You enter wrong format. Please, try again");
            req.getRequestDispatcher("/WEB-INF/jsp/developers/findDeveloperById.jsp").forward(req, resp);
        }
    }
}
