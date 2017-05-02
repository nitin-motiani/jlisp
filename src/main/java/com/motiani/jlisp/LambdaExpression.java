package com.motiani.jlisp;

import java.util.Collections;
import java.util.List;

import com.motiani.jlisp.UserFunctionExpression.UserFunctionArgType;

final class LambdaExpression extends Expression {

	private List<String> argNames;
	private UserFunctionArgType userFunctionArgType;
	private Expression functionBody;

	private LambdaExpression(List<String> argNames,
			UserFunctionArgType userFunctionArgType, Expression body) {
		this.argNames = argNames;
		this.userFunctionArgType = userFunctionArgType;
		this.functionBody = body;
	}

	static LambdaExpression createWithConstArgs(List<String> argNames,
			Expression body) {
		return new LambdaExpression(argNames,
				UserFunctionArgType.CONSTANT_ARGS, body);
	}

	static LambdaExpression crateWithVarArgs(String argName, Expression body) {
		return new LambdaExpression(Collections.singletonList(argName),
				UserFunctionArgType.VARIABLE_ARGS, body);

	}

	Expression evaluate(Scope scope) {
		if (userFunctionArgType.equals(UserFunctionArgType.CONSTANT_ARGS))
			return UserFunctionExpression.createWithConstArgs(functionBody,
					argNames, scope);
		else
			return UserFunctionExpression.createWithVarArgs(functionBody,
					argNames.get(0), scope);
	}
}
