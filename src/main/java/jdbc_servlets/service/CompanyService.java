package jdbc_servlets.service;

import jdbc_servlets.model.dao.CompanyDao;
import jdbc_servlets.model.dto.CompanyDto;
import jdbc_servlets.repository.CompanyRepository;
import jdbc_servlets.service.converter.CompanyConverter;

import java.util.List;
import java.util.stream.Collectors;

public class CompanyService {

    private CompanyRepository repository;
    private CompanyConverter companyConverter;

    public CompanyService(CompanyRepository repository, CompanyConverter companyConverter) {
        this.repository = repository;
        this.companyConverter = companyConverter;
    }

    public CompanyDto create(CompanyDto companyDto) {
        CompanyDao companyDao = repository.create(companyConverter.to(companyDto));
        return companyConverter.from(companyDao);
    }

    public List<CompanyDto> findAll() {
        return repository.findAll().stream()
                .map(companyConverter::from)
                .collect(Collectors.toList());
    }

    public CompanyDto getByID(Integer id) {
        CompanyDao companyDao = repository.getByID(id);
        return companyConverter.from(companyDao);
    }

    public CompanyDto update(CompanyDto companyDto) {
        CompanyDao companyDao = repository.update(companyConverter.to(companyDto));
        return companyConverter.from(companyDao);
    }

    public void delete(CompanyDto companyDto) {
        repository.delete(companyConverter.to(companyDto));
    }

}

