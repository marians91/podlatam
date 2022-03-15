package com.enel.platform.mepodlatam.batch.filters.application.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enel.platform.mepodlatam.batch.filters.application.exception.ApplicationFilterException;

@ExtendWith(MockitoExtension.class)
class ApplicationFilterParserTest {

	
	@Test
	void testApplicationFilterParserByValue1() {	
		String applicationFilter = "field=dtLastModify;value1=20210601";
		ApplicationFilterParser filterParser= new ApplicationFilterParser();
		boolean result = filterParser.doMatch(applicationFilter);
		assertTrue(result);
		assertEquals("dtLastModify", filterParser.getFieldName());
		assertEquals("20210601", filterParser.getFieldValue1());
		assertEquals(Optional.empty(), filterParser.getFieldValue2());
		assertFalse(filterParser.isIncremental());
	}
	
	@Test
	void testApplicationFilterParserByValue1AndValue2() {
		String applicationFilter = "field=dtLastModify;value1=20210601;value2=20210630";
		ApplicationFilterParser filterParser= new ApplicationFilterParser();
		boolean result = filterParser.doMatch(applicationFilter);
		assertTrue(result);
		assertEquals("dtLastModify", filterParser.getFieldName());
		assertEquals("20210601", filterParser.getFieldValue1());
		assertEquals(Optional.of("20210630").get(), filterParser.getFieldValue2().get());
		assertFalse(filterParser.isIncremental());
	}
	
	@Test
	void testApplicationFilterParserByValue1AndValueEmpty() {
		String applicationFilter = "field=dtLastModify;value1=20210601;value2=";
		ApplicationFilterParser filterParser= new ApplicationFilterParser();
		boolean result = filterParser.doMatch(applicationFilter);
		assertTrue(result);
		assertEquals("dtLastModify", filterParser.getFieldName());
		assertEquals("20210601", filterParser.getFieldValue1());
		assertEquals(Optional.ofNullable(""), filterParser.getFieldValue2());
		assertFalse(filterParser.isIncremental());
	}
	
	@Test
	void testApplicationFilterParserIncremental() {
		String applicationFilter = "incremental#field=dtLastModify;value1=20210601";
		ApplicationFilterParser filterParser= new ApplicationFilterParser();
		boolean result = filterParser.doMatch(applicationFilter);
		assertTrue(result);
		assertEquals("dtLastModify", filterParser.getFieldName());
		assertEquals("20210601", filterParser.getFieldValue1());
		assertEquals(Optional.empty(), filterParser.getFieldValue2());
		assertTrue(filterParser.isIncremental());
	}
				
	@Test
	void testApplicationFilterParserFalse() {
		String applicationFilter = "fieldTest=dtLastModify;value1=20210601";
		ApplicationFilterParser filterParser= new ApplicationFilterParser();
		Assertions.assertThrows(ApplicationFilterException.class, () -> {			
			filterParser.doMatch(applicationFilter);
		  });
	}
	
	
	@Test
	void testApplicationIncrementalFilterParserWithAdditionalExpressions() {		
		ApplicationFilterParser filterParser= new ApplicationFilterParser(";?tenant=(\\w+)",false, true);
		String applicationFilter = "incremental#field=dtLastModify;value1=20210601;tenant=ROD1";		
		Assertions.assertThrows(ApplicationFilterException.class, () -> {			
			filterParser.doMatch(applicationFilter);
		  });
	}
	
	@Test
	void testApplicationFilterParserWithOnlyAdditionalExpressions() {		
		ApplicationFilterParser filterParser= new ApplicationFilterParser(";?tenant=(\\w+)",false, true);
		String applicationFilter = "tenant=ROD1";		
		boolean result = filterParser.doMatch(applicationFilter);
		assertTrue(result);
		assertEquals("ROD1",filterParser.getAdditionalFieldAndValues().get("tenant"));

	}
	
	@Test
	void testApplicationFilterParserValuesAndAdditionalExpressions() {		
		ApplicationFilterParser filterParser= new ApplicationFilterParser(";?tenant=(\\w+)",true, false);
		String applicationFilter = "field=dtLastModify;value1=20210601;value2=;tenant=ROD1";		
		boolean result = filterParser.doMatch(applicationFilter);
		assertTrue(result);
		assertTrue(result);
		assertEquals("dtLastModify", filterParser.getFieldName());
		assertEquals("20210601", filterParser.getFieldValue1());
		assertEquals(Optional.ofNullable(""), filterParser.getFieldValue2());
		assertEquals("ROD1",filterParser.getAdditionalFieldAndValues().get("tenant"));

	}
	
	@Test
	void testApplicationFilterParserWithAdditionalExpressionsToIncremental() {		
		ApplicationFilterParser filterParser= new ApplicationFilterParser(";?tenant=(\\w+)",true);
		String applicationFilter = "incremental#field=dtLastModify;value1=20210601;tenant=ROD1";		
		boolean result = filterParser.doMatch(applicationFilter);
		assertTrue(result);
		assertEquals("ROD1",filterParser.getAdditionalFieldAndValues().get("tenant"));
		assertEquals("dtLastModify", filterParser.getFieldName());
		assertEquals("20210601", filterParser.getFieldValue1());
		assertEquals(Optional.empty(), filterParser.getFieldValue2());

	}
	
	@Test
	void testApplicationFilterParserWithAdditionalExpressionsNoIncremental() {		
		ApplicationFilterParser filterParser= new ApplicationFilterParser(";?tenant=(\\w+)",false);
		String applicationFilter = "incremental#field=dtLastModify;value1=20210601";		
		boolean result = filterParser.doMatch(applicationFilter);
		assertTrue(result);
		assertEquals("dtLastModify", filterParser.getFieldName());
		assertEquals("20210601", filterParser.getFieldValue1());
		assertEquals(Optional.empty(), filterParser.getFieldValue2());
		assertTrue(filterParser.isIncremental());
	}
}
