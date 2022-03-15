package com.enel.platform.mepodlatam.batch.repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.enel.platform.mepodlatam.model.PodIncrementalHandler;
import com.google.common.collect.ImmutableList;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PodIncrementalHandlerRepository {

	private final DynamoDB dynamoDB;

	@Value("${amazon.incrementHandlerTable.name}")
	private String tableName;

	private static final String ENTITY_NAME_ATTR = "entityName";
	private static final String FILTER_FIELD_NAME_ATTR = "filterFieldName";
	private static final String FILTER_LAST_VALUE_ATTR = "filterLastValue";
	private static final String DT_MODIFY = "dtModify";

	public PodIncrementalHandler searchByEntityName(String entityName) {
		PodIncrementalHandler mepodIncrementalHandler = new PodIncrementalHandler();
		Table table = dynamoDB.getTable(tableName);
		QuerySpec spec = new QuerySpec().withKeyConditionExpression(ENTITY_NAME_ATTR.concat(" = :entityName"))
				.withValueMap(new ValueMap().withString(":entityName", entityName));

		ItemCollection<QueryOutcome> result = table.query(spec);

		List<Item> items = ImmutableList.copyOf(result.iterator());

		if (items.isEmpty()) {
			return null;
		}

		for (Item item : items) {
			mepodIncrementalHandler.setEntityName(item.get(ENTITY_NAME_ATTR).toString());
			mepodIncrementalHandler.setField(item.get(FILTER_FIELD_NAME_ATTR).toString());
			mepodIncrementalHandler.setValue(item.get(FILTER_LAST_VALUE_ATTR).toString());
			mepodIncrementalHandler.setDtLastModify(item.get(DT_MODIFY).toString());
		}

		return mepodIncrementalHandler;
	}

	public void addEntityLastValue(PodIncrementalHandler mepodIncrementalHandler) {
		Table table = dynamoDB.getTable(tableName);
		Item item = new Item().with(ENTITY_NAME_ATTR, mepodIncrementalHandler.getEntityName())
				.with(FILTER_FIELD_NAME_ATTR, mepodIncrementalHandler.getField())
				.with(FILTER_LAST_VALUE_ATTR, mepodIncrementalHandler.getValue())
				.with(DT_MODIFY, OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).toString());
		table.putItem(item);
	}

	public void modifyEntityLastValue(String entityName, String lastValue) {
		Table table = dynamoDB.getTable(tableName);
		PrimaryKey primaryKey = new PrimaryKey();
		primaryKey.addComponent(ENTITY_NAME_ATTR, entityName);
		AttributeUpdate lastValueAttributeUpdate = new AttributeUpdate(FILTER_LAST_VALUE_ATTR).put(lastValue);
		AttributeUpdate dyModifyAttributeUpdate = new AttributeUpdate(DT_MODIFY).put(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).toString());
		table.updateItem(primaryKey, lastValueAttributeUpdate, dyModifyAttributeUpdate);
	}
	
	public void deleteEntityLastValue(String entityName) {
		Table table = dynamoDB.getTable(tableName);		
		PrimaryKey primaryKey = new PrimaryKey();
		primaryKey.addComponent(ENTITY_NAME_ATTR,entityName);		
        table.deleteItem(primaryKey);
	}
		

}
