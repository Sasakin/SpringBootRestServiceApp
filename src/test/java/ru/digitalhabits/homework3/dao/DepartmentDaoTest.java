package ru.digitalhabits.homework3.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.domain.Person;

import java.util.List;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ComponentScan("ru.digitalhabits.homework3.dao")
class DepartmentDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DepartmentDao departmentDao;

    @Test
    void findById() {

        Department created = entityManager.persist(new Department().setName("VMF").setClosed(false));

        Department d = departmentDao.findById(created.getId());

        assert d.getName() == "VMF";
        assert d.getClosed() == false;
    }

    @Test
    void findAll() {

        entityManager.persist(new Department().setName("SAP").setClosed(false));
        entityManager.persist(new Department().setName("JetBrains").setClosed(false));
        entityManager.persist(new Department().setName("JUG").setClosed(false));

        List<Department> departments = departmentDao.findAll();
        assert departments.size() == 3;
        assert departments.get(0).getName() == "SAP";
        assert departments.get(1).getName() == "JetBrains";
        assert departments.get(2).getName() == "JUG";
    }

    @Test
    void update() {
        Department d = entityManager.persist(new Department().setName("SAP").setClosed(false));

        d.setName("JetBrains").setClosed(true);

        departmentDao.update(d);

        Department checked = departmentDao.findById(d.getId());

        assert checked.getName() == "JetBrains";
        assert checked.getClosed() == true;
    }

    @Test
    void delete() {
        Department created = entityManager.persist(new Department().setName("VMF").setClosed(false));

        Department d = departmentDao.findById(created.getId());

        assert d.getName() == "VMF";
        assert d.getClosed() == false;

        departmentDao.delete(created.getId());

        Department delleted = departmentDao.findById(created.getId());

        assert delleted == null;

    }

}