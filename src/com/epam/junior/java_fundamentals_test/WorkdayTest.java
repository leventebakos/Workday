package com.epam.junior.java_fundamentals_test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.epam.junior.java_fundamentals.Workday;

public class WorkdayTest {
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@Test
	public void test_20150315() {
		String date = "2015-03-15";
		
		Workday.main(new String[]{date});
		assertEquals(date + " is not a workday.", outContent.toString());
	}
	
	@Test
	public void test_20150101() {
		String date = "2015-01-01";
		
		Workday.main(new String[]{date});
		assertEquals(date + " is not a workday.", outContent.toString());
	}
	
	@Test
	public void test_20151224() {
		String date = "2015-12-24";
		
		Workday.main(new String[]{date});
		assertEquals(date + " is not a workday.", outContent.toString());
	}
	
	@Test
	public void test_20150102() {
		String date = "2015-01-02";
		
		Workday.main(new String[]{date});
		assertEquals(date + " is a workday.", outContent.toString());
	}
	
	@Test
	public void test_20141224() {
		String date = "2014-12-24";
		
		Workday.main(new String[]{date});
		assertEquals(date + " is a workday.", outContent.toString());
	}
	
	@Test
	public void test_20120315() {
		String date = "2012-03-15";
		
		Workday.main(new String[]{date});
		assertEquals(date + " is not a workday.", outContent.toString());
	}
	
	@Test
	public void test_20140315() {
		String date = "2014-03-15";
		
		Workday.main(new String[]{date});
		assertEquals(date + " is not a workday.", outContent.toString());
	}
	
	@Test
	public void test_20140101() {
		String date = "2014-01-01";
		
		Workday.main(new String[]{date});
		assertEquals(date + " is not a workday.", outContent.toString());
	}
	
	@Test
	public void test_20151212() {
		String date = "2015-12-12";
		
		Workday.main(new String[]{date});
		assertEquals(date + " is a workday.", outContent.toString());
	}
	
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}

}
