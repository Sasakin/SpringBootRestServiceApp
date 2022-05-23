package ru.charot.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.charot.domain.Department;
import ru.charot.model.DepartmentFullResponse;
import ru.charot.model.DepartmentRequest;
import ru.charot.model.DepartmentShortResponse;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DepartmentBuilder implements Builder<DepartmentRequest, DepartmentFullResponse,
        DepartmentShortResponse, Department> {

    @Autowired
    private PersonBuilder personBuilder;

    @Nonnull
    @Override
    public Department buildFromRequest(DepartmentRequest departmentRequest) {
        return new Department().setName(departmentRequest.getName()).setClosed(false);
    }

    @Nonnull
    @Override
    public DepartmentFullResponse buildResponseFull(Department department) {
        DepartmentFullResponse response = new DepartmentFullResponse();
        response.setId(department.getId()).setName(department.getName()).setClosed(department.getClosed());

        var persons = Optional.ofNullable(department.getPersons())
                .map(people -> people.stream()
                            .map(personBuilder::buildResponseShort)
                            .collect(Collectors.toList())).orElse(new ArrayList<>());
        response.setPersons(persons);

        return response;
    }

    @Nonnull
    @Override
    public DepartmentShortResponse buildResponseShort(Department department) {
        DepartmentShortResponse response = new DepartmentShortResponse();
        response.setId(department.getId()).setName(department.getName());
        return response;
    }
}
