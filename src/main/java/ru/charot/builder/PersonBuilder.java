package ru.charot.builder;

import org.springframework.stereotype.Component;
import ru.charot.domain.Person;
import ru.charot.model.PersonFullResponse;
import ru.charot.model.PersonRequest;
import ru.charot.model.PersonShortResponse;

import javax.annotation.Nonnull;
import java.util.Optional;

@Component
public class PersonBuilder implements Builder<PersonRequest, PersonFullResponse,
        PersonShortResponse, Person> {

    DepartmentBuilder departmentBuilder = new DepartmentBuilder();

    @Nonnull
    @Override
    public Person buildFromRequest(PersonRequest personRequest) {
        Person person = new Person();
        person.setFirstName(personRequest.getFirstName())
                .setMiddleName(personRequest.getMiddleName())
                .setLastName(personRequest.getLastName())
                .setAge(personRequest.getAge());
        return person;
    }

    @Nonnull
    @Override
    public PersonFullResponse buildResponseFull(Person person) {
        PersonFullResponse response = new PersonFullResponse();
        response.setId(person.getId())
                .setFullName(person.getFullName())
                .setAge(person.getAge())
                .setDepartment(Optional.ofNullable(person.getDepartment())
                                .map(d -> departmentBuilder.buildResponseShort(d))
                                .orElse(null));

        return response;
    }

    @Nonnull
    @Override
    public PersonShortResponse buildResponseShort(Person person) {
        PersonShortResponse response = new PersonShortResponse();
        response.setId(person.getId())
                .setFullName(person.getFullName());
        return response;
    }
}
