package jdbc_servlets.service;

import jdbc_servlets.model.dao.DeveloperDao;
import jdbc_servlets.model.dto.DeveloperDto;
import jdbc_servlets.repository.DeveloperRepository;
import jdbc_servlets.service.converter.DeveloperConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeveloperService {

    private DeveloperRepository repository;
    private DeveloperConverter developerConverter;

    public DeveloperService(DeveloperRepository repository, DeveloperConverter developerConverter) {
        this.repository = repository;
        this.developerConverter = developerConverter;
    }

    public DeveloperDto create(DeveloperDto developerDto) {
        DeveloperDao developerDao = repository.create(developerConverter.to(developerDto));
        return developerConverter.from(developerDao);
    }

    public List<DeveloperDto> findAll() {
        return repository.findAll().stream()
                .map(developerConverter::from)
                .collect(Collectors.toList());
    }

    public DeveloperDto getByID(Integer id) {
        DeveloperDao developerDao = repository.getByID(id);
        return developerConverter.from(developerDao);
    }

    public DeveloperDto update(DeveloperDto developerDto) {
        DeveloperDao developerDao = repository.update(developerConverter.to(developerDto));
        return developerConverter.from(developerDao);
    }

    public void delete(DeveloperDto developerDto) {
        repository.delete(developerConverter.to(developerDto));
    }

    public List<DeveloperDto> developersByLanguage(String language) {
        List<DeveloperDto> developerDtoList = new ArrayList<>();
        List<DeveloperDao> list = repository.developersByLanguage(language);
        for (DeveloperDao dao : list) {
            developerDtoList.add(developerConverter.from(dao));
        }
        return developerDtoList;
    }

    public List<DeveloperDto> developersByLevel(String level) {
        List<DeveloperDto> developerDtoList = new ArrayList<>();
        List<DeveloperDao> list = repository.developersByLevel(level);
        for (DeveloperDao dao : list) {
            developerDtoList.add(developerConverter.from(dao));
        }
        return developerDtoList;
    }

    public List<DeveloperDto> getListOfProjectDevelopers(String projectName) {
        List<DeveloperDto> developerDtoList = new ArrayList<>();
        List<DeveloperDao> listOfProjectDevelopers = repository.quantityOfDevelopersInProject(projectName);
        for (DeveloperDao dao: listOfProjectDevelopers) {
            developerDtoList.add(developerConverter.from(dao));
        }
        return developerDtoList;
    }
}

