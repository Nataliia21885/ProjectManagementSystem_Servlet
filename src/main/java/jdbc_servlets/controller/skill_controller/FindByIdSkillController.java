package jdbc_servlets.controller.skill_controller;

import jdbc_servlets.config.DatabaseManagerConnector;
import jdbc_servlets.config.PropertiesConfig;
import jdbc_servlets.model.dto.SkillDto;
import jdbc_servlets.repository.SkillRepository;
import jdbc_servlets.service.SkillService;
import jdbc_servlets.service.converter.SkillConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@WebServlet(urlPatterns = "/skills/findId")
public class FindByIdSkillController extends HttpServlet {
    private SkillService skillService;

    @Override
    public void init() throws ServletException {
        String dbPassword = System.getenv("dbpassword");
        String dbUserName = System.getenv("dbusername");
        PropertiesConfig propertiesConfig = new PropertiesConfig();
        Properties properties = propertiesConfig.loadProperties("application.properties");
        DatabaseManagerConnector dbManager = new DatabaseManagerConnector(properties, dbUserName, dbPassword);
        SkillRepository skillRepository = new SkillRepository(dbManager);
        SkillConverter skillConverter = new SkillConverter();
        skillService = new SkillService(skillRepository, skillConverter);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            SkillDto skill = skillService.getByID(id);
            if (skill.equals(new SkillDto())) {
                req.setAttribute("message", "Skill with such id does not find");
                req.getRequestDispatcher("/WEB-INF/jsp/skills/findSkillById.jsp").forward(req, resp);
            } else {
                req.setAttribute("skill", skill);
                req.getRequestDispatcher("/WEB-INF/jsp/skills/findSkillById.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("message", "You enter wrong format. Please, try again");
            req.getRequestDispatcher("/WEB-INF/jsp/skills/findSkillById.jsp").forward(req, resp);
        }
    }
}
