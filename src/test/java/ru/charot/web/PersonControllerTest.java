package ru.charot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.charot.model.DepartmentShortResponse;
import ru.charot.model.PersonFullResponse;
import ru.charot.model.PersonRequest;
import ru.charot.model.PersonShortResponse;
import ru.charot.service.DepartmentService;
import ru.charot.service.PersonService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DepartmentService departmentService;

    @MockBean
    PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final int personId = 10;
    private static final int personAge = 33;
    private static final int amountPeople = 3;
    private static final String departmentName = "testDepartment";
    private static final String firstName = "testName";
    private static final String middleName = "testMiddleName";
    private static final String lastName = "testLastName";

    @Test
    void persons() throws Exception {
        List<PersonShortResponse> personShortResponseList = new ArrayList<>();
        for(int i = 0; i < amountPeople; i++){
            PersonShortResponse personShortResponse = new PersonShortResponse()
                    .setId(personId + i)
                    .setFullName(firstName + " " + middleName + " " + lastName + i);
            personShortResponseList.add(personShortResponse);
        }

        Mockito.when(personService.findAll()).thenReturn(personShortResponseList);

        mockMvc.perform(get("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(personShortResponseList.get(0).getId()))
                .andExpect(jsonPath("$[0].fullName").value(personShortResponseList.get(0).getFullName()));
    }

    @Test
    void person() throws Exception {
        PersonFullResponse personFullResponse = new PersonFullResponse()
                .setId(personId)
                .setFullName(firstName + " " + middleName + " " + lastName)
                .setAge(personAge);

        DepartmentShortResponse departmentShortResponse = new DepartmentShortResponse()
                .setName(departmentName)
                .setName(departmentName);

        personFullResponse.setDepartment(departmentShortResponse);

        Mockito.when(personService.getById(Mockito.anyInt())).thenReturn(personFullResponse);

        mockMvc.perform(get("/api/v1/persons/" + personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(personFullResponse.getId()))
                .andExpect(jsonPath("$.fullName").value(personFullResponse.getFullName()))
                .andExpect(jsonPath("$.age").value(personFullResponse.getAge()))
                .andExpect(jsonPath("$.department").value(personFullResponse.getDepartment()));
    }

    @Test
    void createPerson() throws Exception {
        PersonRequest personRequest = new PersonRequest()
                .setAge(personAge)
                .setFirstName(firstName)
                .setMiddleName(middleName)
                .setLastName(lastName);

        Mockito.when(personService.create(personRequest)).thenReturn(personId);

        mockMvc.perform(post("/api/v1/persons")
                        .content(objectMapper.writeValueAsString(personRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()); // or 201
    }

    @Test
    void updatePerson() throws Exception {
        PersonFullResponse personFullResponse = new PersonFullResponse()
                .setId(personId)
                .setAge(personAge)
                .setFullName(firstName + " " + middleName + " " + lastName);

        DepartmentShortResponse departmentShortResponse = new DepartmentShortResponse()
                .setName(departmentName)
                .setName(departmentName);

        personFullResponse.setDepartment(departmentShortResponse);

        PersonRequest request = new PersonRequest();
        request.setFirstName("abbc");
        request.setMiddleName(middleName);
        request.setLastName(lastName);
        request.setAge(personAge);

        Mockito.when(personService.update(personId, request)).thenReturn(personFullResponse);

        mockMvc.perform(patch("/api/v1/persons/" + personId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // or 200
    }

    @Test
    void deletePerson() throws Exception {
        mockMvc.perform(delete("/api/v1/persons/" + personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }
}