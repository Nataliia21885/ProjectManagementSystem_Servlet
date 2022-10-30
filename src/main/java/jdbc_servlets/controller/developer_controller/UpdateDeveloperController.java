package jdbc_servlets.controller.developer_controller;

import jdbc_servlets.config.DatabaseManagerConnector;
import jdbc_servlets.config.PropertiesConfig;
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

@WebServlet(urlPatterns = "/developers/upd")
public class UpdateDeveloperController extends HttpServlet {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("name");
            int age = Integer.parseInt(req.getParameter("age"));
            String sex = req.getParameter("sex");
            int salary = Integer.parseInt(req.getParameter("salary"));
            DeveloperDto findById = developerService.getByID(id);
            if (!name.isBlank() && !sex.isBlank() && findById.getId() != null
                    && findById.getAge() != null && findById.getSalary() != null) {
                DeveloperDto developer = new DeveloperDto(id, name, age, sex, salary);
                DeveloperDto updatedDeveloper = developerService.update(developer);
                req.setAttribute("updatedDeveloper", updatedDeveloper);
                req.setAttribute("message", "Developer is updated successfully");
                req.getRequestDispatcher("/WEB-INF/jsp/developers/updateDeveloper.jsp").forward(req, resp);
            } else {
                req.setAttribute("message", "Developer is not updated (fields are empty or entering id does not exist)");
                req.getRequestDispatcher("/WEB-INF/jsp/developers/updateDeveloper.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("message", "You enter wrong format. Please, try again");
            req.getRequestDispatcher("/WEB-INF/jsp/developers/updateDeveloper.jsp").forward(req, resp);
        }
    }
}
