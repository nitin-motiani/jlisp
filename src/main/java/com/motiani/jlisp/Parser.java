package com.motiani.jlisp;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class Parser {

	// The reason for return linked list is that we can easily peek top elements
	// and pop them from a linked list
	private LinkedList<String> tokanize(String exp) {
		if (exp == null)
			throw new IllegalArgumentException("Can't parse null expression");
		String expModified = exp.replace("(", " ( ").replace(")", " ) ");
		return Arrays.stream(expModified.split(" ")).collect(
				Collectors.toCollection(LinkedList::new));s
	}

	private Expression parse(LinkedList<String> tokens) {
		return null;
	}

	private NumberExpression parseNumber(String token) throws ParseException {
		// TODO: Have some helper function for this
		return new NumberExpression(NumberFormat.getInstance().parse(token));
	}

	private BooleanExpression parseBoolean(String token) {
		// TODO: Will throw null pointer exception this one for incorrect input.
		// So needs to be handled
		return new BooleanExpression(Boolean.parseBoolean(token));
	}

	private VariableExpression parseVariable(String token) {
		// TODO: Check for valid variable
		return new VariableExpression(token);
	}

	private StringExpression parseString(String token) {
		// TODO: Check for valid string (quotes probably)
		return new StringExpression(token);
	}

	private DefinitionExpression parseDefinition(LinkedList<String> tokens) {
		return null;
	}

	private AssignmentExpression parseAssignment(LinkedList<String> tokens) {
		return null;
	}

	private ConditionalExpression parseConditional(LinkedList<String> tokens) {
		return null;
	}

	private ListExpression parseList(LinkedList<String> tokens) {
		return null;
	}

	private LambdaExpression parseLambda(LinkedList<String> tokens) {
		return null;
	}

	Expression parse(String expStr) {
		if (expStr == null)
			throw new IllegalArgumentException("Can't parse null expression");
		LinkedList<String> tokens = tokanize(expStr);
		return parse(tokens);
	}
}
