package com.motiani.jlisp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.NumberUtils;

class Parser {

	// TODO : See if we can get rid of this
	private final List<String> reserved = Arrays.asList("(", ")");

	// The reason for return type linked list is that we can easily peek top
	// elements and pop them from a linked list
	private LinkedList<String> tokanize(String exp) {
		if (exp == null)
			throw new IllegalArgumentException("Can't parse null expression");
		String expModified = exp.replace("(", " ( ").replace(")", " ) ");
		return Arrays.stream(expModified.split(" "))
				// This is a shit hack
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toCollection(LinkedList::new));
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

	private boolean isValidSymbol(String token) {
		return (!reserved.contains(token.toLowerCase()));
	}

	private Expression parseAtomicExpression(String token) {
		if (isValidNumber(token))
			return parseNumber(token);

		if (isValidBoolean(token))
			return parseBoolean(token);

		if (isValidSymbol(token))
			return parseSymbol(token);

		throw new IllegalArgumentException("Invalid atomic token " + token);
	}

	private Expression parseListExpression(LinkedList<String> tokens) {
		// TODO: Should check that it has minimum number of elements required
		// for a list expression
		if (!tokens.get(0).equals("("))
			throw new IllegalArgumentException(
					"The list expression should start with (");

		List<Type> items = parseList(tokens);
		return ListExpressionFactory.createListExpression(items);
	}

	private NumberExpression parseNumber(String token) {
		return new NumberExpression(new BigDecimal(token));
	}

	private BooleanExpression parseBoolean(String token) {
		return new BooleanExpression(Boolean.parseBoolean(token));
	}

	private SymbolExpression parseSymbol(String token) {
		// TODO: Check for valid symbol i.e. based on allowed characters etc.
		return new SymbolExpression(token);
	}

	private List<Type> parseList(LinkedList<String> tokens) {
		List<Type> expressions = new ArrayList<>();
		tokens.removeFirst();
		while (tokens.size() > 0 && !tokens.get(0).equals(")")) {
			expressions.add(parse(tokens));
		}

		if (tokens.size() == 0)
			throw new IllegalArgumentException("Parantheses mismatch");

		tokens.removeFirst();

		return expressions;

	}

	Expression parse(String expStr) {
		if (expStr == null)
			throw new IllegalArgumentException("Can't parse null expression");
		LinkedList<String> tokens = tokanize(expStr);
		Expression expression = parse(tokens);

		// We should've parsed all the tokens by this point.
		if (tokens.size() > 0) {
			throw new IllegalArgumentException("Invalid token " + tokens.get(0));
		}
		return expression;
	}
}
