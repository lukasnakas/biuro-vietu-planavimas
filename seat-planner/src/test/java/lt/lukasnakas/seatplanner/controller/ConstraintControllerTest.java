package lt.lukasnakas.seatplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.lukasnakas.seatplanner.helper.ConstraintDataHelper;
import lt.lukasnakas.seatplanner.model.dto.ConstraintDto;
import lt.lukasnakas.seatplanner.model.request.ConstraintAlterationRequest;
import lt.lukasnakas.seatplanner.service.ConstraintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConstraintControllerTest {

    @Mock
    private ConstraintService constraintService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ConstraintController(constraintService)).build();
    }

    @Test
    void shouldFetchConstraints() throws Exception {
        List<ConstraintDto> constraints = new ArrayList<>();
        given(constraintService.getAllConstraintDtos()).willReturn(constraints);
        String responseBody = new ObjectMapper().writeValueAsString(constraints);

        mockMvc.perform(get("/v1/planner/constraints"))
                .andExpect(content().string(responseBody))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFetchConstraint() throws Exception {
        String constraintId = "1";
        ConstraintDto constraintDto = new ConstraintDto();
        given(constraintService.getConstraintDtoById(constraintId)).willReturn(constraintDto);
        String responseBody = new ObjectMapper().writeValueAsString(constraintDto);

        mockMvc.perform(get("/v1/planner/constraints/{id}", constraintId))
                .andExpect(content().string(responseBody))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestResponseWhenRequestInvalid() throws Exception {
        ConstraintAlterationRequest request = new ConstraintAlterationRequest();
        String requestBody = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(patch("/v1/planner/constraints")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateConstraint() throws Exception {
        ConstraintAlterationRequest request = ConstraintDataHelper.getValidConstraintAlterationRequest();
        ConstraintDto constraintDto = new ConstraintDto();
        given(constraintService.updateConstraint(request)).willReturn(constraintDto);
        String requestBody = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(patch("/v1/planner/constraints")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isOk());
    }
}
