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

@WebServlet(urlPatterns = "/developers")
public class CreateDeveloperController extends HttpServlet {
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
        String name = req.getParameter("name");
        int age = Integer.parseInt(req.getParameter("age"));
        String sex = req.getParameter("sex");
        int salary = Integer.parseInt(req.getParameter("salary"));
        req.setAttribute("name", name);
        req.setAttribute("age", age);
        req.setAttribute("sex", sex);
        req.setAttribute("salary", salary);
        req.getRequestDispatcher("/WEB-INF/jsp/developers/createDeveloper.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        int age = Integer.parseInt(req.getParameter("age"));
        String sex = req.getParameter("sex");
        int salary = Integer.parseInt(req.getParameter("salary"));
        DeveloperDto developer = new DeveloperDto(name, age, sex, salary);
        if(!developer.getName().isBlank() || developer.getAge() != null
                || !developer.getSex().isBlank() || developer.getSalary() != null){
            developerService.create(developer);
            req.setAttribute("developer", developer);
            req.setAttribute("message", "Developer successfully created");
            req.getRequestDispatcher("/WEB-INF/jsp/developers/createDeveloper.jsp").forward(req, resp);
        } else {
            req.setAttribute("message", "Developer isn't created");
            req.getRequestDispatcher("/WEB-INF/jsp/developers/createDeveloper.jsp").forward(req, resp);
        }
    }
}
