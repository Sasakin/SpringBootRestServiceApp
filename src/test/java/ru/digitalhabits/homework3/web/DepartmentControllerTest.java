package ru.digitalhabits.homework3.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.digitalhabits.homework3.model.DepartmentFullResponse;
import ru.digitalhabits.homework3.model.DepartmentRequest;
import ru.digitalhabits.homework3.model.DepartmentShortResponse;
import ru.digitalhabits.homework3.service.DepartmentService;
import ru.digitalhabits.homework3.service.PersonService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DepartmentService departmentService;

    @MockBean
    PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final int departmentId = 10;
    private static final String departmentName = "testDepartment";
    private static final int amountDepartments = 4;
    private static final int amountPeopleInDepartment = 3;
    private static final int personId = 2;


    @Test
    void departments() throws Exception {
        List<DepartmentShortResponse> departmentList = new ArrayList<>();
        for(int i = 0; i < amountDepartments; i++){
            DepartmentShortResponse departmentShortResponse = new DepartmentShortResponse()
                    .setId(departmentId + i)
                    .setName(departmentName + i);
            departmentList.add(departmentShortResponse);
        }

        Mockito.when(departmentService.findAll()).thenReturn(departmentList);

        mockMvc.perform(get("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(departmentList.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(departmentList.get(0).getName()));
    }

    @Test
    void department() throws Exception {
        DepartmentFullResponse departmentFullResponse = new DepartmentFullResponse()
                .setName(departmentName)
                .setId(departmentId)
                .setClosed(false);

        Mockito.when(departmentService.getById(Mockito.anyInt())).thenReturn(departmentFullResponse);

        mockMvc.perform(get("/api/v1/departments/" + departmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(departmentFullResponse.getId()))
                .andExpect(jsonPath("$.name").value(departmentFullResponse.getName()))
                .andExpect(jsonPath("$.closed").value(departmentFullResponse.isClosed()));

    }

    @Test
    void createDepartment() throws Exception {
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName(departmentName);

        Mockito.when(departmentService.create(departmentRequest)).thenReturn(departmentId);

        mockMvc.perform(post("/api/v1/departments")
                        .content(objectMapper.writeValueAsString(departmentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }

    @Test
    void updateDepartment() throws Exception {
        DepartmentFullResponse departmentFullResponse = new DepartmentFullResponse()
                .setName(departmentName)
                .setId(departmentId)
                .setClosed(false);

        DepartmentRequest request = new DepartmentRequest();
        request.setName("abbc");

        Mockito.when(departmentService.update(departmentId, request)).thenReturn(departmentFullResponse);

        mockMvc.perform(patch("/api/v1/departments/" + departmentId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(departmentFullResponse.getId()))
                .andExpect(jsonPath("$.name").value(departmentFullResponse.getName()))
                .andExpect(jsonPath("$.closed").value(departmentFullResponse.isClosed()));

    }

    @Test
    void deleteDepartment() throws Exception {
        mockMvc.perform(delete("/api/v1/departments/" + departmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }

    @Test
    void addPersonToDepartment() throws Exception {
        mockMvc.perform(post("/api/v1/departments/" + departmentId + "/" + personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }

    @Test
    void removePersonToDepartment() throws Exception {
        mockMvc.perform(delete("/api/v1/departments/" + departmentId + "/" + personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }

    @Test
    void closeDepartment() throws Exception {
        mockMvc.perform(post("/api/v1/departments/" + departmentId + "/close")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }
}