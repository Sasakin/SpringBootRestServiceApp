package ru.charot.dao;

import org.springframework.data.repository.NoRepositoryBean;
import ru.charot.domain.Department;

@NoRepositoryBean
public interface DepartmentDao
        extends CrudOperations<Department, Integer> {}
