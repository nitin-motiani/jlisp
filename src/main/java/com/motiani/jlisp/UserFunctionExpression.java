package com.motiani.jlisp;

import java.util.Collections;
import java.util.List;

final class UserFunctionExpression extends FunctionExpression {

	private static enum UserFunctionArgType {
		CONSTANT_ARGS, VARIABLE_ARGS
	}

	private Expression body;
	private List<String> argNames;
	private UserFunctionArgType userFunctionArgType;
	private Scope parentScope;

	private UserFunctionExpression(Expression body, List<String> argNames,
			UserFunctionArgType userFunctionArgType, Scope parentScope) {
		this.body = body;
		this.argNames = argNames;
		this.userFunctionArgType = userFunctionArgType;
		this.parentScope = parentScope;
	}

	static UserFunctionExpression createWithConstArgs(Expression body,
			List<String> argNames, Scope parentScope) {
		return new UserFunctionExpression(body, argNames,
				UserFunctionArgType.CONSTANT_ARGS, parentScope);
	}

	static UserFunctionExpression createWithVarArgs(Expression body,
			String argName, Scope parentScope) {
		return new UserFunctionExpression(body,
				Collections.singletonList(argName),
				UserFunctionArgType.VARIABLE_ARGS, parentScope);
	}

	private Scope createScope(List<Expression> args) {
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
	public Expression call(List<Expression> args) {
		Scope scope = createScope(args);
		return body.evaluate(scope);
	}

}
