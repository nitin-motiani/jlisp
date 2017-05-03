package com.motiani.jlisp;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

class Parser {

	private List<String> tokanize(String exp) {
		return new ArrayList<>();
	}

	private Expression parse(List<String> tokens, int index) {
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

	private DefinitionExpression parseDefinition(List<String> tokens, int index) {
		return null;
	}

	private AssignmentExpression parseAssignment(List<String> tokens, int index) {
		return null;
	}

	private ConditionalExpression parseConditional(List<String> tokens,
			int index) {
		return null;
	}

	private ListExpression parseList(List<String> tokens, int index) {
		return null;
	}

	private LambdaExpression parseLambda(List<String> tokens, int index) {
		return null;
	}

	Expression parse(String expStr) {
		List<String> tokens = tokanize(expStr);
		return parse(tokens, 0);
	}
}
