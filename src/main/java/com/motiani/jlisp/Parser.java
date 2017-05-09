package com.motiani.jlisp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.NumberUtils;

import com.motiani.jlisp.UserFunctionExpression.UserFunctionArgType;

class Parser {

	private final List<String> reserved = Arrays.asList("if", "define", "set!",
			"(", "lambda");

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

	private boolean isValidVariable(String token) {
		return (!reserved.contains(token.toLowerCase()));
	}

	private Expression parseAtomicExpression(String token) {
		if (isValidNumber(token))
			return parseNumber(token);

		if (isValidBoolean(token))
			return parseBoolean(token);

		if (isValidVariable(token))
			return parseVariable(token);

		throw new IllegalArgumentException("Invalid atomic token " + token);
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
		return new NumberExpression(new BigDecimal(token));
	}

	private BooleanExpression parseBoolean(String token) {
		return new BooleanExpression(Boolean.parseBoolean(token));
	}

	private VariableExpression parseVariable(String token) {
		// TODO: Check for valid variable
		return new VariableExpression(token);
	}

	private DefinitionExpression parseDefinition(LinkedList<String> tokens) {
		tokens.removeFirst();
		tokens.removeFirst();
		String symbol = tokens.get(0);
		if (!isValidVariable(symbol))
			throw new RuntimeException("Invalid variable name " + symbol);

		tokens.removeFirst();

		Expression valueExpr = parse(tokens);
		if (!tokens.get(0).equals(")"))
			throw new IllegalArgumentException("Parantheses mismatch");

		tokens.removeFirst();

		return new DefinitionExpression(symbol, valueExpr);
	}

	// TODO: Get rid of copied code between definition and assignment
	private AssignmentExpression parseAssignment(LinkedList<String> tokens) {
		tokens.removeFirst();
		tokens.removeFirst();
		String symbol = tokens.get(0);
		if (!isValidVariable(symbol))
			throw new RuntimeException("Invalid variable name " + symbol);

		tokens.removeFirst();

		Expression valueExpr = parse(tokens);
		if (!tokens.get(0).equals(")"))
			throw new IllegalArgumentException("Parantheses mismatch");

		tokens.removeFirst();

		return new AssignmentExpression(symbol, valueExpr);
	}

	private ConditionalExpression parseConditional(LinkedList<String> tokens) {
		tokens.removeFirst();
		tokens.removeFirst();

		Expression condition = parse(tokens);
		Expression ifExpr = parse(tokens);
		Expression elseExpr = parse(tokens);

		if (!tokens.get(0).equals(")"))
			throw new IllegalArgumentException("Parantheses mismatch");

		tokens.removeFirst();

		return new ConditionalExpression(condition, ifExpr, elseExpr);
	}

	private ListExpression parseList(LinkedList<String> tokens) {
		tokens.removeFirst();
		tokens.removeFirst();

		List<Expression> expressions = new ArrayList<>();
		while (tokens.size() > 0 && !tokens.get(0).equals(")")) {
			expressions.add(parse(tokens));
		}

		if (tokens.size() == 0)
			throw new IllegalArgumentException("Parantheses mismatch");

		tokens.removeFirst();

		return new ListExpression(expressions);
	}

	private LambdaExpression parseLambda(LinkedList<String> tokens) {
		// TODO: The repetition of these two lines everywhere is pretty bad.
		// I need to think and restructure code to handle these
		tokens.removeFirst();
		tokens.removeFirst();

		UserFunctionArgType argType;
		List<String> args;
		if (tokens.get(0).equals("(")) {
			argType = UserFunctionArgType.CONSTANT_ARGS;
			args = new ArrayList<>();
			tokens.removeFirst();
			while (tokens.size() > 0 && !tokens.get(0).equals(")")) {
				args.add(tokens.removeFirst());
			}

			if (tokens.size() == 0)
				throw new IllegalArgumentException("Parantheses mismatch");
		} else {
			argType = UserFunctionArgType.VARIABLE_ARGS;
			args = Collections.singletonList(tokens.removeFirst());
		}

		List<Expression> body = new ArrayList<>();

		// TODO: Again very much repeatation of the code from list expression.
		// Need to restructure.
		while (tokens.size() > 0 && !tokens.get(0).equals(")")) {
			body.add(parse(tokens));
		}

		if (tokens.size() == 0)
			throw new IllegalArgumentException("Parantheses mismatch");

		tokens.removeFirst();

		if (argType == UserFunctionArgType.CONSTANT_ARGS) {
			return LambdaExpression.createWithConstArgs(args, body);
		} else {
			return LambdaExpression.createWithVarArgs(args.get(0), body);
		}

	}

	Expression parse(String expStr) {
		if (expStr == null)
			throw new IllegalArgumentException("Can't parse null expression");
		LinkedList<String> tokens = tokanize(expStr);
		return parse(tokens);
	}
}
