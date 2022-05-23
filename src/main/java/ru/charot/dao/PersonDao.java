package ru.charot.dao;

import org.springframework.data.repository.NoRepositoryBean;
import ru.charot.domain.Person;

@NoRepositoryBean
public interface PersonDao
        extends CrudOperations<Person, Integer> {}