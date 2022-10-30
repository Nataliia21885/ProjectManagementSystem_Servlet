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

@WebServlet(urlPatterns = "/skills/upd")
public class UpdateSkillController extends HttpServlet {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String language = req.getParameter("language");
            String level = req.getParameter("level");
            SkillDto findById = skillService.getByID(id);
            if (!language.isBlank() && !level.isBlank() && findById.getId() != null ) {
                SkillDto skill = new SkillDto(id, language, level);
                SkillDto updatedSkill = skillService.update(skill);
                req.setAttribute("updatedSkill", updatedSkill);
                req.setAttribute("message", "Skill is updated successfully");
                req.getRequestDispatcher("/WEB-INF/jsp/skills/updateSkill.jsp").forward(req, resp);
            } else {
                req.setAttribute("message", "Skill is not updated (fields are empty or entering id does not exist)");
                req.getRequestDispatcher("/WEB-INF/jsp/skills/updateSkill.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("message", "You enter wrong format. Please, try again");
            req.getRequestDispatcher("/WEB-INF/jsp/skills/updateSkill.jsp").forward(req, resp);
        }
    }
}
