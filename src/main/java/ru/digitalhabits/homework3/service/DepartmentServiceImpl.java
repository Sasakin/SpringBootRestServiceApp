package ru.digitalhabits.homework3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.digitalhabits.homework3.builder.DepartmentBuilder;
import ru.digitalhabits.homework3.dao.DepartmentDao;
import ru.digitalhabits.homework3.dao.PersonDao;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.domain.Person;
import ru.digitalhabits.homework3.model.DepartmentFullResponse;
import ru.digitalhabits.homework3.model.DepartmentRequest;
import ru.digitalhabits.homework3.model.DepartmentShortResponse;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl
        implements DepartmentService {

    @Autowired
    private DepartmentBuilder builder;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private PersonDao personDao;

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentShortResponse> findAll() {
        return departmentDao.findAll().stream().map(builder::buildResponseShort).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public DepartmentFullResponse getById(int id) {
        Optional<DepartmentFullResponse> resp = Optional.ofNullable(departmentDao.findById(id))
                .map(builder::buildResponseFull);
        if (resp.isPresent()) {
            return resp.get();
        }
        throw new EntityNotFoundException("Department not found");
    }

    @Override
    @Transactional
    public int create(@Nonnull DepartmentRequest request) {
        Department create = builder.buildFromRequest(request);
        return departmentDao.create(create);
    }

    @Nonnull
    @Override
    @Transactional
    public DepartmentFullResponse update(int id, @Nonnull DepartmentRequest request) {
        Department fromBD = departmentDao.findById(id);
        if(fromBD == null) {
            throw new EntityNotFoundException("Department not found");
        }

        fromBD.setName(request.getName());

        departmentDao.update(fromBD);

        return builder.buildResponseFull(fromBD);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Department department = departmentDao.findById(id);
        if (department == null) {
            return;
        }

        List<Person> persons = department.getPersons();
        if(persons != null) {
            persons.forEach(person -> {
                person.setDepartment(null);
                personDao.update(person);
            });
        }

        departmentDao.delete(id);
    }

    @Override
    @Transactional
    public void close(int id) {
        // TODO: NotImplemented: удаление всех людей из департамента и установка отметки на департаменте,
        //  что он закрыт для добавления новых людей. Если не найдено, отдавать 404:NotFound
        Department department = departmentDao.findById(id);
        if (department == null) {
            throw new EntityNotFoundException("Department not found");
        }

        List<Person> persons = department.getPersons();
        if(persons != null) {
            persons.forEach(person -> {
                person.setDepartment(null);
                personDao.update(person);
            });
        }

        department.setPersons(new ArrayList<>());
        department.setClosed(true);
        departmentDao.update(department);
    }
}
