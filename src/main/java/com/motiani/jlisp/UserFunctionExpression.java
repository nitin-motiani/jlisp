package com.motiani.jlisp;

import java.util.Collections;
import java.util.List;

final class UserFunctionExpression extends Function {

	static enum UserFunctionArgType {
		CONSTANT_ARGS, VARIABLE_ARGS
	}

	// Initially this was just one expression, but changed it so that our
	// function can handle multiple expressions.
	// This should be a list of expressions, not a ListExpression. We want these
	// to be evaluated one by one
	private List<Expression> body;
	private List<String> argNames;
	private UserFunctionArgType userFunctionArgType;
	private Scope parentScope;

	private UserFunctionExpression(List<Expression> body,
			List<String> argNames, UserFunctionArgType userFunctionArgType,
			Scope parentScope) {
		this.body = body;
		this.argNames = argNames;
		this.userFunctionArgType = userFunctionArgType;
		this.parentScope = parentScope;
	}

	// TODO: Don't particularly like the fact that both lambda and user function
	// expressions have copied code for some of this stuff. Will possibly
	// refactor that to make slightly cleaner at some point
	static UserFunctionExpression createWithConstArgs(List<Expression> body,
			List<String> argNames, Scope parentScope) {
		return new UserFunctionExpression(body, argNames,
				UserFunctionArgType.CONSTANT_ARGS, parentScope);
	}

	static UserFunctionExpression createWithVarArgs(List<Expression> body,
			String argName, Scope parentScope) {
		return new UserFunctionExpression(body,
				Collections.singletonList(argName),
				UserFunctionArgType.VARIABLE_ARGS, parentScope);
	}

	private Scope createScope(List<Type> args) {
		Scope evaluationScope = new Scope(parentScope);
		if (userFunctionArgType.equals(UserFunctionArgType.CONSTANT_ARGS)) {
			if (args.size() != this.argNames.size())
				throw new RuntimeException("Incorrect number of arguments");

			int numArgs = argNames.size();
			for (int i = 0; i < numArgs; i++)
				evaluationScope.create(argNames.get(i), args.get(i));
		} else {
			evaluationScope.create(argNames.get(0), new ListExpression(args));
		}

		return evaluationScope;
	}

	@Override
	public Type call(List<Type> args) {
		Scope scope = createScope(args);
		Type solution = null;
		for (Expression expr : body) {
			solution = expr.evaluate(scope);
		}

		// Return the result of last expression
		return solution;
	}

}
