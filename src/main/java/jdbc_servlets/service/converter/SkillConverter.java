package jdbc_servlets.service.converter;

import jdbc_servlets.model.dao.SkillDao;
import jdbc_servlets.model.dto.SkillDto;

public class SkillConverter implements Converter<SkillDto, SkillDao> {

    @Override
    public SkillDto from(SkillDao entity) {
        SkillDto skillDto = new SkillDto();
        skillDto.setId(entity.getId());
        skillDto.setLanguage(entity.getLanguage());
        skillDto.setLevel(entity.getLevel());
        return skillDto;
    }

    @Override
    public SkillDao to(SkillDto entity) {
        SkillDao skillDao = new SkillDao();
        skillDao.setId(entity.getId());
        skillDao.setLanguage(entity.getLanguage());
        skillDao.setLevel(entity.getLevel());
        return skillDao;
    }
}