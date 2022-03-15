package com.enel.platform.mepodlatam.batch.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.enel.platform.mepodlatam.batch.controller.support.MapperJson;
import com.enel.platform.mepodlatam.batch.service.PodIncrementalHandlerService;
import com.enel.platform.mepodlatam.dto.IncrementalFilterDTO;
import com.enel.platform.mepodlatam.dto.IncrementalFilterDTO.EntityFilterDTO;

public abstract class PodRequestIncrementalControllerTest {

	@MockBean
	private PodIncrementalHandlerService visitRequestIncrementalHandlerService;

	private final MockMvc mockMvc;
	private final String entityName;
	private final String entityVersion;
	private final String processId = "123456789";

	public PodRequestIncrementalControllerTest(MockMvc mockMvc, String entityName, String entityVersion) {
		this.mockMvc = mockMvc;
		this.entityName = entityName;
		this.entityVersion = entityVersion;
	}

	@Test
	void getProcessState() throws Exception {
		IncrementalFilterDTO incrementalFilterExp = new IncrementalFilterDTO();
		EntityFilterDTO entityFilter = new EntityFilterDTO();
		entityFilter.setField("dtWoaCreation");
		entityFilter.setValue("2020-01-01T15:00:00");
		incrementalFilterExp.setEntityFilter(entityFilter);
		when(visitRequestIncrementalHandlerService.findLastValue(entityName)).thenReturn(incrementalFilterExp);
		ResultActions res = mockMvc.perform(MockMvcRequestBuilders
				.get("/batch/{batch.api.name}/{version}/process-state/{process-id}", entityName, entityVersion,
						processId)
				.contentType(MediaType.APPLICATION_JSON).content(MapperJson.mapToJson(incrementalFilterExp)));
		res.andExpect(MockMvcResultMatchers.status().isOk());
		IncrementalFilterDTO incrementalFilter = MapperJson
				.mapFromJson(res.andReturn().getResponse().getContentAsString(), IncrementalFilterDTO.class);
		assertEquals(incrementalFilterExp.getEntityFilter().getField(), incrementalFilter.getEntityFilter().getField());
		assertEquals(incrementalFilterExp.getEntityFilter().getValue(), incrementalFilter.getEntityFilter().getValue());

	}

	@Test
	void putprocessState() throws Exception {
		IncrementalFilterDTO incrementalFilter = new IncrementalFilterDTO();
		EntityFilterDTO entityFilter = new EntityFilterDTO();
		entityFilter.setField("dtWoaCreation");
		entityFilter.setValue("2020-01-01T15:00:00");
		incrementalFilter.setEntityFilter(entityFilter);
		doNothing().when(visitRequestIncrementalHandlerService).updateOrCreateLastValue(entityName, incrementalFilter);
		ResultActions res = mockMvc.perform(MockMvcRequestBuilders
				.put("/batch/{batch.api.name}/{version}/process-state/{process-id}", entityName, entityVersion,
						processId)
				.contentType(MediaType.APPLICATION_JSON).content(MapperJson.mapToJson(incrementalFilter)));
		res.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	void deletLastValueExtract() throws Exception {
		IncrementalFilterDTO incrementalFilter = new IncrementalFilterDTO();
		EntityFilterDTO entityFilter = new EntityFilterDTO();
		entityFilter.setField("dtWoaCreation");
		entityFilter.setValue("2020-01-01T15:00:00");
		incrementalFilter.setEntityFilter(entityFilter);
		doNothing().when(visitRequestIncrementalHandlerService).deleteLastValue(entityName);
		ResultActions res = mockMvc.perform(MockMvcRequestBuilders
				.delete("/batch/{batch.api.name}/{version}/process-state", entityName, entityVersion)
				.contentType(MediaType.APPLICATION_JSON).content(MapperJson.mapToJson(incrementalFilter)));
		res.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
