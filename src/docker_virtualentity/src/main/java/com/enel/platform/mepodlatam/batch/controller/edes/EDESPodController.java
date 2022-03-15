package com.enel.platform.mepodlatam.batch.controller.edes;

import java.sql.Date;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enel.platform.entity.service.batch.BatchService;
import com.enel.platform.mepodlatam.batch.controller.PodIncrementalController;
import com.enel.platform.mepodlatam.batch.service.PodIncrementalHandlerService;
import com.enel.platform.mepodlatam.json.adapter.DateAdapter;
import com.enel.platform.mepodlatam.json.adapter.edes.EDESZonedDateTimeAdapter;
import com.enel.platform.mepodlatam.model.edes.EDESPod;
import com.google.gson.GsonBuilder;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/batch/${batch.api.EDES.mepodlatam.name}/${batch.api.EDES.mepodlatam.version}")
@Tag(name = "batch", description = "EDES Pod Latam Api entity operation")
public class EDESPodController extends PodIncrementalController<EDESPod> {

	public EDESPodController(BatchService<EDESPod> batchService,
			@Value("${batch.api.EDES.mepodlatam.name}") String entityName,
			PodIncrementalHandlerService podIncrementalHandlerService) {
		super(batchService, entityName, podIncrementalHandlerService);
	}
	
	@Override
	protected GsonBuilder getGsonBuilder() {
		return new GsonBuilder().disableHtmlEscaping().serializeNulls()
				.registerTypeAdapter(ZonedDateTime.class, new EDESZonedDateTimeAdapter())
				.registerTypeAdapter(Date.class, new DateAdapter());
	}


}
