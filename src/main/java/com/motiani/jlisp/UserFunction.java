package com.motiani.jlisp;

import java.util.Collections;
import java.util.List;

final class UserFunction extends Function {

	static enum UserFunctionArgType {
		CONSTANT_ARGS, VARIABLE_ARGS
	}

	// Initially this was just one expression, but changed it so that our
	// function can handle multiple expressions.
	// This should be a list of expressions, not a ListExpression. We want these
	// to be evaluated one by one
	private List<Expression> body;
	private List<SymbolExpression> argNames;
	private UserFunctionArgType userFunctionArgType;
	private Scope parentScope;

	private UserFunction(List<Expression> body,
			List<SymbolExpression> argNames,
			UserFunctionArgType userFunctionArgType, Scope parentScope) {
		this.body = body;
		this.argNames = argNames;
		this.userFunctionArgType = userFunctionArgType;
		this.parentScope = parentScope;
	}

	static UserFunction createWithConstArgs(List<Expression> body,
			List<SymbolExpression> argNames, Scope parentScope) {
		return new UserFunction(body, argNames,
				UserFunctionArgType.CONSTANT_ARGS, parentScope);
	}

	static UserFunction createWithVarArgs(List<Expression> body,
			SymbolExpression argName, Scope parentScope) {
		return new UserFunction(body,
				Collections.singletonList(argName),
				UserFunctionArgType.VARIABLE_ARGS, parentScope);
	}

	private Scope createScope(List<Data> args) {
		Scope evaluationScope = new Scope(parentScope);
		if (userFunctionArgType.equals(UserFunctionArgType.CONSTANT_ARGS)) {
			if (args.size() != this.argNames.size())
				throw new RuntimeException("Incorrect number of arguments");

			int numArgs = argNames.size();
			for (int i = 0; i < numArgs; i++)
				evaluationScope.create(argNames.get(i), args.get(i));
		} else {
			evaluationScope.create(argNames.get(0),
					ListExpressionFactory.createListExpression(args));
		}

		return evaluationScope;
	}

	@Override
	Data call(List<Data> args) {
		Scope scope = createScope(args);
		Data solution = null;
		for (Expression expr : body) {
			solution = expr.evaluate(scope);
		}

		// Return the result of last expression
		return solution;
	}

}
