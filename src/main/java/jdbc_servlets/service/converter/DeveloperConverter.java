package jdbc_servlets.service.converter;

import jdbc_servlets.model.dao.DeveloperDao;
import jdbc_servlets.model.dto.DeveloperDto;

public class DeveloperConverter implements Converter<DeveloperDto, DeveloperDao> {

    @Override
    public DeveloperDto from(DeveloperDao entity) {
        DeveloperDto developerDto = new DeveloperDto();
        developerDto.setId(entity.getId());
        developerDto.setName(entity.getName());
        developerDto.setAge(entity.getAge());
        developerDto.setSex(entity.getSex());
        developerDto.setSalary(entity.getSalary());
        return developerDto;
    }

    @Override
    public DeveloperDao to(DeveloperDto entity) {
        DeveloperDao developerDao = new DeveloperDao();
        developerDao.setId(entity.getId());
        developerDao.setName(entity.getName());
        developerDao.setAge(entity.getAge());
        developerDao.setSex(entity.getSex());
        developerDao.setSalary(entity.getSalary());
        return developerDao;
    }
}
