package ru.digitalhabits.homework3.builder;

import org.springframework.stereotype.Component;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.model.DepartmentFullResponse;
import ru.digitalhabits.homework3.model.DepartmentRequest;
import ru.digitalhabits.homework3.model.DepartmentShortResponse;

@Component
public class DepartmentBuilder implements Builder<DepartmentRequest, DepartmentFullResponse,
        DepartmentShortResponse, Department> {

    @Override
    public Department buildFromRequest(DepartmentRequest departmentRequest) {
        return new Department().setName(departmentRequest.getName());
    }

    @Override
    public DepartmentFullResponse buildResponseFull(Department department) {
        DepartmentFullResponse response = new DepartmentFullResponse();
        response.setId(department.getId()).setName(department.getName()).setClosed(department.getClosed());

        return response;
    }

    @Override
    public DepartmentShortResponse buildResponseShort(Department department) {
        DepartmentShortResponse response = new DepartmentShortResponse();
        response.setId(department.getId()).setName(department.getName());
        return response;
    }
}
