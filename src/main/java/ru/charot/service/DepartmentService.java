package ru.charot.service;

import ru.charot.model.DepartmentFullResponse;
import ru.charot.model.DepartmentRequest;
import ru.charot.model.DepartmentShortResponse;

import javax.annotation.Nonnull;
import java.util.List;

public interface DepartmentService {

    @Nonnull
    List<DepartmentShortResponse> findAll();

    @Nonnull
    DepartmentFullResponse getById(int id);

    int create(@Nonnull DepartmentRequest request);

    @Nonnull
    DepartmentFullResponse update(int id, @Nonnull DepartmentRequest request);

    void delete(int id);

    void close(int id);
}
