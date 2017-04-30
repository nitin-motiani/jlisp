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

	@Override
	public Expression call(Expression... args) {
		// TODO Auto-generated method stub
		return null;
	}

}
