package com.motiani.jlisp;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.NumberUtils;

class Parser {

	// The reason for return linked list is that we can easily peek top elements
	// and pop them from a linked list
	private LinkedList<String> tokanize(String exp) {
		if (exp == null)
			throw new IllegalArgumentException("Can't parse null expression");
		String expModified = exp.replace("(", " ( ").replace(")", " ) ");
		return Arrays.stream(expModified.split(" ")).collect(
				Collectors.toCollection(LinkedList::new));
	}

	private Expression parse(LinkedList<String> tokens) {
		// TODO: Need to see if null is the right thing to do here
		if (CollectionUtils.isEmpty(tokens))
			return null;

		if (!tokens.get(0).equals("(")) {
			String token = tokens.removeFirst();
			return parseAtomicExpression(token);
		}

		return parseListExpression(tokens);
	}

	private boolean isValidNumber(String token) {
		return NumberUtils.isNumber(token);
	}

	private boolean isValidBoolean(String token) {
		return (token.toLowerCase().equals("true") || token.toLowerCase()
				.equals("false"));
	}

	private Expression parseAtomicExpression(String token) {
		if (isValidNumber(token))
			return parseNumber(token);

		if (isValidBoolean(token))
			return parseBoolean(token);

		return parseVariable(token);
	}

	private Expression parseListExpression(LinkedList<String> tokens) {
		// TODO: Should check that it has minimum number of elements required
		// for a list expression
		if (!tokens.get(0).equals("("))
			throw new IllegalArgumentException(
					"The list expression should start with (");

		switch (tokens.get(1).toLowerCase()) {
		case "if":
			return parseConditional(tokens);
		case "define":
			return parseDefinition(tokens);
		case "set!":
			return parseAssignment(tokens);
		case "lambda":
			return parseLambda(tokens);
		default:
			return parseList(tokens);
		}
	}

	private NumberExpression parseNumber(String token) {
		try {
			return new NumberExpression(NumberFormat.getInstance().parse(token));
		} catch (ParseException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	private BooleanExpression parseBoolean(String token) {
		return new BooleanExpression(Boolean.parseBoolean(token));
	}

	private VariableExpression parseVariable(String token) {
		// TODO: Check for valid variable
		return new VariableExpression(token);
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
