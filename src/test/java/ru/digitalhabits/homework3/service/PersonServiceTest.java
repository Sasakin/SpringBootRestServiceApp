package ru.digitalhabits.homework3.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.digitalhabits.homework3.dao.DepartmentDao;
import ru.digitalhabits.homework3.dao.PersonDao;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.domain.Person;
import ru.digitalhabits.homework3.model.PersonFullResponse;
import ru.digitalhabits.homework3.model.PersonRequest;
import ru.digitalhabits.homework3.model.PersonShortResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureTestDatabase
class PersonServiceTest {


    @Autowired
    private PersonService service;

    @MockBean
    private PersonDao personDao;

    @MockBean
    private DepartmentDao departmentDao;

    private static final int countPersons = 3;

    private List<Person> getPersonListForTest() {
        List<Person> personList = new ArrayList<>();
        for(int i = 0; i < countPersons; i++){
            Person person = new Person()
                    .setId(i)
                    .setFirstName("first" + i)
                    .setMiddleName("second" + i)
                    .setLastName("last" + i)
                    .setAge(i + 18);

            personList.add(person);
        }

        return personList;
    }

    private Person getPersonForTest() {
        Person person = new Person()
                .setId(1)
                .setFirstName("Igor")
                .setMiddleName("Michailovich")
                .setLastName("Zamjatin")
                .setAge(20);
        return person;
    }

    private Department getDepartmentForTest() {
        Department department = new Department();
        department.setId(1);
        department.setName("abc");
        department.setClosed(false);
        department.setPersons(new ArrayList<>());
        return department;
    }

    @Test
    void findAll() {
        List<Person> personList = getPersonListForTest();

        Mockito.when(personDao.findAll()).thenReturn(personList);

        List<PersonShortResponse> personResponse = service.findAll();

        Assertions.assertNotNull(personResponse);
        Assertions.assertEquals(countPersons, personResponse.size());

        for(int i = 0; i < countPersons; i++){
            Person person = personList.get(i);
            PersonShortResponse pResp = new PersonShortResponse();
            pResp.setId(person.getId());
            pResp.setFullName(person.getFullName());
            Assertions.assertTrue(personResponse.contains(pResp));
        }
    }

    @Test
    void findById() {
        Person person = getPersonForTest();

        int personId = person.getId();
        String fullName = person.getFullName();
        int age = person.getAge();

        Mockito.when(personDao.findById(personId)).thenReturn(person);

        PersonFullResponse response = service.getById(person.getId());
        Assertions.assertEquals(personId, response.getId());
        Assertions.assertEquals(fullName, response.getFullName());
        Assertions.assertEquals(age, response.getAge());
    }

    @Test
    void create() {
        Person person = getPersonForTest();

        PersonRequest request = new PersonRequest();
        request.setFirstName(person.getFirstName());
        request.setMiddleName(person.getMiddleName());
        request.setLastName(person.getLastName());
        request.setAge(person.getAge());

        Mockito.when(personDao.create(any(Person.class))).thenReturn(person.getId());

        int id = service.create(request);

        Assertions.assertEquals(person.getId(), id);
    }

    @Test
    void update() {
        Person person = getPersonForTest();

        PersonRequest request = new PersonRequest();
        request.setFirstName(person.getFirstName())
                .setMiddleName(person.getMiddleName())
                .setLastName(person.getLastName())
                .setAge(person.getAge());


        Mockito.when(personDao.findById(any(Integer.class))).thenReturn(person);
        Mockito.when(personDao.update(any(Person.class))).thenReturn(person);

        PersonFullResponse resp = service.update(person.getId(), request);

        Assertions.assertNotNull(resp);

        Assertions.assertEquals(resp.getFullName(), person.getFullName());
    }

    @Test
    void delete() {
        Person person = getPersonForTest();

        Mockito.when(personDao.update(person)).thenReturn(person);
        Mockito.when(personDao.findById(person.getId())).thenReturn(person);

        service.delete(person.getId());

    }

    @Test
    void addPersonToDepartment() {
        Person person = getPersonForTest();
        Department department = getDepartmentForTest();

        Mockito.when(personDao.findById(person.getId())).thenReturn(person);

        Mockito.when(departmentDao.findById(department.getId())).thenReturn(department);

        Mockito.when(departmentDao.update(department)).thenReturn(department);

        service.addPersonToDepartment(department.getId(), person.getId());

        Assertions.assertNotNull(person.getDepartment());
    }

    @Test
    void removePersonFromDepartment() {
        Department department = getDepartmentForTest();
        Person person = getPersonForTest();
        Optional.ofNullable(department.getPersons()).map(people -> people.add(person));

        Mockito.when(departmentDao.findById(department.getId())).thenReturn(department);
        Mockito.when(personDao.update(person)).thenReturn(person);

        service.removePersonFromDepartment(department.getId(), person.getId());

        Assertions.assertNull(person.getDepartment());
    }
}