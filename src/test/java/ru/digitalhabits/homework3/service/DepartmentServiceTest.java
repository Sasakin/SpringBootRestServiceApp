package ru.digitalhabits.homework3.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import ru.digitalhabits.homework3.dao.DepartmentDao;
import ru.digitalhabits.homework3.dao.PersonDao;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.domain.Person;
import ru.digitalhabits.homework3.model.DepartmentFullResponse;
import ru.digitalhabits.homework3.model.DepartmentRequest;
import ru.digitalhabits.homework3.model.DepartmentShortResponse;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureTestDatabase
class DepartmentServiceTest {

    @Autowired
    private DepartmentService service;

    @MockBean
    private DepartmentDao departmentDao;

    @MockBean
    private PersonDao personDao;

    private static final int departmentId = 10;
    private static final String departmentName = "testDepartment";
    private static final int countDepartments = 4;
    private static final int countPeopleInDepartment = 3;

    @Test
    void findAll() {
        List<Department> departmentList = new ArrayList<>();
        for(int i = 0; i < countDepartments; i++){
            Department department = new Department()
                    .setName("abc" + i)
                    .setClosed(false);
            departmentList.add(department);
        }

        Mockito.when(departmentDao.findAll()).thenReturn(departmentList);

        List<DepartmentShortResponse> departmentResponse = service.findAll();
        Assertions.assertEquals(countDepartments, departmentResponse.size());

        for(int i = 0; i < countDepartments; i++){
            Department department = departmentList.get(i);
            DepartmentShortResponse dResp = new DepartmentShortResponse();
            dResp.setId(department.getId());
            dResp.setName(department.getName());
            Assertions.assertTrue(departmentResponse.contains(dResp));
        }
    }

    @Test
    void findById() {

        Department department = new Department();
        department.setId(departmentId);
        department.setName(departmentName);
        department.setClosed(false);

        Mockito.when(departmentDao.findById(departmentId)).thenReturn(department);

        DepartmentFullResponse fromBD = service.getById(departmentId);
        Assertions.assertEquals(departmentId, fromBD.getId());
        Assertions.assertEquals(departmentName, fromBD.getName());
        Assertions.assertFalse(fromBD.isClosed());

    }

    @Test
    void create() {
        Department department = new Department();
        department.setId(departmentId);
        department.setName(departmentName);
        department.setClosed(false);

        DepartmentRequest request = new DepartmentRequest();
        request.setName(departmentName);

        Mockito.when(departmentDao.create(any(Department.class))).thenReturn(departmentId);

        int id = service.create(request);

        Assertions.assertEquals(departmentId, id);

    }

    @Test
    void update() {
        Department department = new Department();
        department.setId(departmentId);
        department.setName(departmentName);
        department.setClosed(false);

        DepartmentRequest request = new DepartmentRequest();
        request.setName(departmentName);

        Mockito.when(departmentDao.findById(any(Integer.class))).thenReturn(department);
        Mockito.when(departmentDao.update(any(Department.class))).thenReturn(department);

        DepartmentFullResponse resp = service.update(departmentId, request);

        Assertions.assertNotNull(resp);

        Assertions.assertEquals(resp.getName(), departmentName);

    }

    @Test
    void delete() {
        Department department = new Department();
        department.setId(departmentId);
        department.setName(departmentName);
        department.setClosed(false);

        List<Person> personList = new ArrayList<>();
        for(int i = 0; i < countPeopleInDepartment; i++){
            Person person = new Person()
                    .setFirstName("first" + i)
                    .setMiddleName("midleName" + i)
                    .setLastName("lastName" + i)
                    .setAge(20 + i);
            personList.add(person);
            Mockito.when(personDao.update(person)).thenReturn(person.setDepartment(null));
        }
        department.setPersons(personList);
        Mockito.when(departmentDao.findById(departmentId)).thenReturn(department);
        service.delete(departmentId);

        for(int i = 0; i < countPeopleInDepartment; i++){
            Assertions.assertNull(department.getPersons().get(i).getDepartment());
        }

    }

    @Test
    void close() {
        Department department = new Department();
        department.setId(departmentId);
        department.setName(departmentName);
        department.setClosed(false);

        List<Person> personList = new ArrayList<>();
        for(int i = 0; i < countPeopleInDepartment; i++){
            Person person = new Person()
                    .setFirstName("first" + i)
                    .setMiddleName("midleName" + i)
                    .setLastName("lastName" + i)
                    .setAge(20 + i);
            personList.add(person);
            Mockito.when(personDao.update(person)).thenReturn(person.setDepartment(null));
        }
        department.setPersons(personList);
        Mockito.when(departmentDao.findById(departmentId)).thenReturn(department);
        Assertions.assertFalse(department.getClosed());
        Assertions.assertNotNull(department.getPersons());
        service.close(departmentId);

        Assertions.assertTrue(department.getClosed());
        Assertions.assertTrue(department.getPersons() == null || department.getPersons().isEmpty());
    }
}