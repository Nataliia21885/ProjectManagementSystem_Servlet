package jdbc_servlets.controller.project_controller;

import jdbc_servlets.config.DatabaseManagerConnector;
import jdbc_servlets.config.PropertiesConfig;
import jdbc_servlets.model.dto.CompanyDto;
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

@WebServlet(urlPatterns = "/projects/upd")
public class UpdateProjectController extends HttpServlet {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String projectName = req.getParameter("project_name");
            String country = req.getParameter("country");
            int companyId = Integer.parseInt(req.getParameter("company_id"));
            int customerId = Integer.parseInt(req.getParameter("customer_id"));
            int cost = Integer.parseInt(req.getParameter("cost"));
            Date dateOfCreation = Date.valueOf(req.getParameter("date_of_creation"));
            ProjectDto findById = projectService.getByID(id);
            if (!projectName.isBlank() && !country.isBlank() && companyId != 0
                    && customerId != 0 && cost != 0 && dateOfCreation != null) {
                ProjectDto project = new ProjectDto(id, projectName, country, companyId, customerId, cost, dateOfCreation);
                ProjectDto updatedProject = projectService.update(project);
                req.setAttribute("updatedProject", updatedProject);
                req.setAttribute("message", "Project is updated successfully");
                req.getRequestDispatcher("/WEB-INF/jsp/projects/updateProject.jsp").forward(req, resp);
            } else {
                req.setAttribute("message", "Project is not updated (fields are empty or entering id does not exist)");
                req.getRequestDispatcher("/WEB-INF/jsp/projects/updateProject.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("message", "You enter wrong format. Please, try again");
            req.getRequestDispatcher("/WEB-INF/jsp/projects/updateProject.jsp").forward(req, resp);
        }
    }
}
