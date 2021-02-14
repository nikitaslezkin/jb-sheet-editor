package com.slezkin;

import static org.junit.Assert.assertEquals;
import com.slezkin.сalculator.Parser;
import org.junit.Test;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CalculationTest {

	@Test
	public void Test() {

		String function = "2+3*3+xx1";
		List<String> vars = new LinkedList<>(Arrays.asList("xx1"));
		List<Double> values = new LinkedList<>(Arrays.asList(5.0));
		String result = Parser.eval(function, vars, values);
		assertEquals("16.0", result);

		function = "-sin(pi/2)";
		vars = new LinkedList<>(Arrays.asList("x"));
		values = new LinkedList<>(Arrays.asList(2.0));
		result = Parser.eval(function, vars, values);
		assertEquals("-1.0", result);

		function = "x+2^3-sin(pi/2)";
		vars = new LinkedList<>(Arrays.asList("x"));
		values = new LinkedList<>(Arrays.asList(2.0));
		result = Parser.eval(function, vars, values);
		assertEquals("9.0", result);

		function = "x*y-x^y+y^x";
		vars = new LinkedList<>(Arrays.asList("x", "y"));
		values = new LinkedList<>(Arrays.asList(3.0, -2.0));
		result = Parser.eval(function, vars, values);
		assertEquals("-14.11111111111111", result);

		function = "pow(sin(a), 2)+pow(cos(a),2) + pow(sin(b), 2)+pow(cos(b),2)";
		vars = new LinkedList<>(Arrays.asList("a", "b"));
		values = new LinkedList<>(Arrays.asList(1.0, 1.5));
		result = Parser.eval(function, vars, values);
		assertEquals("2.0", result);

		function = "-(a-(a-(b-(b-(c)))))";
		vars = new LinkedList<>(Arrays.asList("a", "b", "c"));
		values = new LinkedList<>(Arrays.asList(4.0, 5.0, 6.0));
		result = Parser.eval(function, vars, values);
		assertEquals("-6.0", result);

		function = "2*(-(((z*3)*sqrt(x^(2)))+3))";
		vars = new LinkedList<>(Arrays.asList("x", "z"));
		values = new LinkedList<>(Arrays.asList(2.0, 1.0));
		result = Parser.eval(function, vars, values);
		assertEquals("-18.0", result);

		function = "sin(1)";
		vars = new LinkedList<>();
		values = new LinkedList<>();
		result = Parser.eval(function, vars, values);
		assertEquals("0.8414709848078965", result);

		System.out.println("Test End");
	}
}
