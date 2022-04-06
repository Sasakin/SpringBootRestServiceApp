package ru.digitalhabits.homework3.dao;

import org.springframework.stereotype.Repository;
import ru.digitalhabits.homework3.domain.Department;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository()
public class DepartmentDaoImpl
        implements DepartmentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Nonnull
    @Override
    public void create(@Nonnull Department entity) {
        entityManager.persist(entity);
    }

    @Nullable
    @Override
    public Department findById(@Nonnull Integer id) {
        return entityManager.find(Department.class, id);
    }

    @Nonnull
    @Override
    public List<Department> findAll() {
        return entityManager.createQuery("select d from Department d", Department.class).getResultList();
    }

    @Nonnull
    @Override
    public Department update(@Nonnull Department department) {
        entityManager.createQuery("update Department d " +
                        "set d.name = ?1," +
                        "    d.closed = ?2")
                .setParameter(1, department.getName())
                .setParameter(2, department.getClosed())
                .executeUpdate();

        return findById(department.getId());
    }

    @Nullable
    @Override
    public Department delete(@Nonnull Integer id) {
        entityManager.remove(findById(id));
        return null;
    }
}
