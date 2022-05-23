package ru.charot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.charot.builder.PersonBuilder;
import ru.charot.dao.DepartmentDao;
import ru.charot.dao.PersonDao;
import ru.charot.domain.Person;
import ru.charot.domain.Department;
import ru.charot.model.PersonFullResponse;
import ru.charot.model.PersonRequest;
import ru.charot.model.PersonShortResponse;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl
        implements PersonService {

    @Autowired
    private PersonBuilder builder;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<PersonShortResponse> findAll() {
        return personDao.findAll().stream().map(builder::buildResponseShort).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public PersonFullResponse getById(int id) {
        Optional<PersonFullResponse> resp = Optional.ofNullable(personDao.findById(id))
                .map(builder::buildResponseFull);
        if (resp.isPresent()) {
            return resp.get();
        }
        throw new EntityNotFoundException("Department not found");
    }

    @Override
    @Transactional
    public int create(@Nonnull PersonRequest request) {
        Person create = builder.buildFromRequest(request);
        return personDao.create(create);
    }

    @Nonnull
    @Override
    @Transactional
    public PersonFullResponse update(int id, @Nonnull PersonRequest request) {
        Person fromBD = personDao.findById(id);
        if(fromBD == null) {
            throw new EntityNotFoundException("Person not found");
        }

        Person forUpdate = builder.buildFromRequest(request).setId(id);

        personDao.update(forUpdate);

        return builder.buildResponseFull(forUpdate);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Person person = personDao.findById(id);
        if(person == null)
            return;

        person.setDepartment(null);

        personDao.update(person);

        personDao.delete(person.getId());
    }


    @Override
    @Transactional
    public void addPersonToDepartment(int departmentId, int personId) {
        // TODO: NotImplemented: добавление нового человека в департамент.
        //  Если не найден человек или департамент, отдавать 404:NotFound.
        //  Если департамент закрыт, то отдавать 409:Conflict

        Person person = personDao.findById(personId);
        if(person == null) {
            throw new EntityNotFoundException("Person not found");
        }

        Department department = departmentDao.findById(departmentId);
        if(department == null) {
            throw new EntityNotFoundException("Department not found");
        }

        if(department.isClosed()) {
            throw new IllegalStateException("Department is closed");
        }

        person.setDepartment(department);

        personDao.update(person);
    }

    @Override
    @Transactional
    public void removePersonFromDepartment(int departmentId, int personId) {
        //  Если департамент не найден, отдавать 404:NotFound, если не найден человек в департаменте, то ничего не делать

        Department department = departmentDao.findById(departmentId);
        if (department == null) {
            throw new EntityNotFoundException("Department not found");
        }

        List<Person> persons = department.getPersons();
        if(persons != null) {
            persons.stream().filter(person -> person.getId() == personId).forEach(person -> {
                person.setDepartment(null);
                personDao.update(person);
            });
        }
    }
}
