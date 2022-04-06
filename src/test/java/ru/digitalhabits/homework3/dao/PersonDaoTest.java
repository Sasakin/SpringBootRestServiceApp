package ru.digitalhabits.homework3.dao;

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
class PersonDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonDao personDao;

    @Test
    void findById() {
        Person created = entityManager.persist(new Person()
                .setFirstName("Andrey").setMiddleName("Evgenievich")
                .setLastName("Stepanov").setAge(60));

        Person checked = personDao.findById(created.getId());

        assert checked.getFirstName() == "Andrey";
        assert checked.getMiddleName() == "Evgenievich";
        assert checked.getLastName() == "Stepanov";
        assert checked.getAge() == 60;
    }

    @Test
    void findAll() {
        entityManager.persist(new Person()
                .setFirstName("Andrey").setMiddleName("Evgenievich")
                .setLastName("Stepanov").setAge(10));
        entityManager.persist(new Person()
                .setFirstName("Grigori").setMiddleName("Ivanovich")
                .setLastName("Razanov").setAge(20));
        entityManager.persist(new Person()
                .setFirstName("Petr").setMiddleName("Felemonovich")
                .setLastName("Laysan").setAge(30));

        List<Person> persons = personDao.findAll();

        assert persons.size() == 3;

        Person p1 = persons.get(0);
        assert p1.getFirstName() == "Andrey";
        assert p1.getMiddleName() == "Evgenievich";
        assert p1.getLastName() == "Stepanov";
        assert p1.getAge() == 10;

        Person p2 = persons.get(1);
        assert p2.getFirstName() == "Grigori";
        assert p2.getMiddleName() == "Ivanovich";
        assert p2.getLastName() == "Razanov";
        assert p2.getAge() == 20;

        Person p3 = persons.get(2);
        assert p3.getFirstName() == "Petr";
        assert p3.getMiddleName() == "Felemonovich";
        assert p3.getLastName() == "Laysan";
        assert p3.getAge() == 30;

    }

    @Test
    void update() {
        Person updated = entityManager.persist(new Person()
                .setFirstName("Andrey").setMiddleName("Evgenievich")
                .setLastName("Stepanov").setAge(10));

        updated.setFirstName("Grigori").setMiddleName("Ivanovich").setLastName("Razanov").setAge(20);

        personDao.update(updated);

        Person checked = personDao.findById(updated.getId());

        assert checked.getFirstName() == "Grigori";
        assert checked.getMiddleName() == "Ivanovich";
        assert checked.getLastName() == "Razanov";
        assert checked.getAge() == 20;
    }

    @Test
    void delete() {
        Person created = entityManager.persist(new Person()
                .setFirstName("Andrey").setMiddleName("Evgenievich")
                .setLastName("Stepanov").setAge(10));

        Person forDelete = personDao.findById(created.getId());

        assert forDelete.getFirstName() == "Andrey";
        assert forDelete.getMiddleName() == "Evgenievich";
        assert forDelete.getLastName() == "Stepanov";
        assert forDelete.getAge() == 10;

        personDao.delete(forDelete.getId());

        Person delleted = personDao.findById(forDelete.getId());

        assert delleted == null;
    }
}