package jdbc_servlets.controller.project_controller;

import jdbc_servlets.config.DatabaseManagerConnector;
import jdbc_servlets.config.PropertiesConfig;
import jdbc_servlets.model.dto.ProjectDto;
import jdbc_servlets.repository.ProjectRepository;
import jdbc_servlets.service.ProjectService;
import jdbc_servlets.service.converter.ProjectConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.Objects;
import java.util.Properties;

@WebServlet(urlPatterns = "/projects")
public class CreateProjectController extends HttpServlet {
    private ProjectService projectService;

    @Override
    public void init() throws ServletException {
        String dbPassword = System.getenv("dbpassword");
        String dbUserName = System.getenv("dbusername");
        PropertiesConfig propertiesConfig = new PropertiesConfig();
        Properties properties = propertiesConfig.loadProperties("application.properties");
        DatabaseManagerConnector dbManager = new DatabaseManagerConnector(properties, dbUserName, dbPassword);
        ProjectRepository projectRepository = new ProjectRepository(dbManager);
        ProjectConverter projectConverter = new ProjectConverter();
        projectService = new ProjectService(projectRepository, projectConverter);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String projectName = req.getParameter("project_name");
        String country = req.getParameter("country");
        int companyId = Integer.parseInt(req.getParameter("company_id"));
        int customerId = Integer.parseInt(req.getParameter("customer_id"));
        int cost = Integer.parseInt(req.getParameter("cost"));
        Date dateOfCreation = Date.valueOf(req.getParameter("date_of_creation"));
        req.setAttribute("project_name", projectName);
        req.setAttribute("country", country);
        req.setAttribute("company_id", companyId);
        req.setAttribute("customer_id", customerId);
        req.setAttribute("cost", cost);
        req.setAttribute("date_of_creation", dateOfCreation);
        req.getRequestDispatcher("/WEB-INF/jsp/projects/createProject.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String projectName = req.getParameter("project_name");
        String country = req.getParameter("country");
        int companyId = Integer.parseInt(req.getParameter("company_id"));
        int customerId = Integer.parseInt(req.getParameter("customer_id"));
        int cost = Integer.parseInt(req.getParameter("cost"));
        Date dateOfCreation = Date.valueOf(req.getParameter("date_of_creation"));
        ProjectDto project = new ProjectDto(projectName, country, companyId, customerId, cost, dateOfCreation);
        if(!project.getProjectName().isBlank() || !project.getCountry().isBlank() || project.getCompanyId() != null
        || project.getCustomerId() != null || project.getCost() != null || Objects.isNull(project.getDateOfCreation())){
            projectService.create(project);
            req.setAttribute("project", project);
            req.setAttribute("message", "Project successfully created");
            req.getRequestDispatcher("/WEB-INF/jsp/projects/createProject.jsp").forward(req, resp);
        } else {
            req.setAttribute("message", "Project isn't created");
            req.getRequestDispatcher("/WEB-INF/jsp/projects/createProject.jsp").forward(req, resp);
        }
    }
}
