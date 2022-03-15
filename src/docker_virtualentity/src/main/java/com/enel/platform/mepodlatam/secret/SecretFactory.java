package com.enel.platform.mepodlatam.secret;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecretFactory implements FactoryBean<DBSecret> {

	private String[] attributeNames;
	private JsonNode nodes;

	@Override
	public DBSecret getObject() throws Exception {
		if (attributeNames.length == 0) {
			throw new IllegalArgumentException("No attribute name for secret");
		}
		
		if (nodes == null) {
			throw new IllegalArgumentException("Json is empty");
		}
		return buildDBSecret();
	}

	@Override
	public Class<?> getObjectType() {
		return DBSecret.class;
	}
	
	@Override
    public boolean isSingleton() {
        return false;
    }

	private DBSecret buildDBSecret() {
		DBSecret secret = new DBSecret();
		Field[] secretFields = DBSecret.class.getDeclaredFields();
		for (Field field : secretFields) {
			String secretAttr = getSecretAttributetName(field.getName());
			if(secretAttr != null) {
				String fieldValue = nodes.get(secretAttr).asText();
				ReflectionUtils.makeAccessible(field);
				ReflectionUtils.setField(field, secret, fieldValue);
			}
		}
		return secret;
	}

	private String getSecretAttributetName(String fieldName) {
		return Arrays.asList(attributeNames).stream().filter(a -> StringUtils.endsWithIgnoreCase(a, fieldName))
				.findFirst().orElse(null);
	}
}
