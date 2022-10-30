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

@WebServlet(urlPatterns = "/skills")
public class CreateSkillController extends HttpServlet {
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
        String language = req.getParameter("language");
        String level = req.getParameter("level");
        req.setAttribute("language", language);
        req.setAttribute("level", level);
        req.getRequestDispatcher("/WEB-INF/jsp/skills/createSkill.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String language = req.getParameter("language");
        String level = req.getParameter("level");
        SkillDto skill = new SkillDto(language, level);
        if(!skill.getLanguage().isBlank() || !skill.getLevel().isBlank()){
            skillService.create(skill);
            req.setAttribute("skill", skill);
            req.setAttribute("message", "Skill successfully created");
            req.getRequestDispatcher("/WEB-INF/jsp/skills/createSkill.jsp").forward(req, resp);
        } else {
            req.setAttribute("message", "Skill isn't created");
            req.getRequestDispatcher("/WEB-INF/jsp/skills/createSkill.jsp").forward(req, resp);
        }
    }
}
