package ru.digitalhabits.homework3.dao;

import org.springframework.stereotype.Repository;
import ru.digitalhabits.homework3.domain.Person;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PersonDaoImpl
        implements PersonDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Nonnull
    @Override
    public int create(@Nonnull Person entity) {
        entityManager.persist(entity);
        return entity.getId();
    }

    @Nullable
    @Override
    public Person findById(@Nonnull Integer id) {
        return entityManager.find(Person.class, id);
    }

    @Nonnull
    @Override
    public List<Person> findAll() {
        return entityManager.createQuery("select p from Person p", Person.class).getResultList();
    }

    @Nonnull
    @Override
    public Person update(@Nonnull Person person) {
        entityManager.createQuery("update Person p " +
                        "set p.firstName = ?1," +
                        "    p.lastName = ?2," +
                        "    p.middleName = ?3," +
                        "    p.age = ?4")
                .setParameter(1, person.getFirstName())
                .setParameter(2, person.getLastName())
                .setParameter(3, person.getMiddleName())
                .setParameter(4, person.getAge())
                .executeUpdate();

        return findById(person.getId());
    }

    @Nullable
    @Override
    public Person delete(@Nonnull Integer id) {
        /*entityManager.createQuery("delete from Person p where p.id = ?1")
                .setParameter(1, id).executeUpdate();*/
        entityManager.remove(findById(id));
        return null;
    }
}
