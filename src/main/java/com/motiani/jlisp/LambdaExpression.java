package com.motiani.jlisp;

import java.util.Collections;
import java.util.List;

import com.motiani.jlisp.UserFunctionExpression.UserFunctionArgType;

final class LambdaExpression extends Expression {

	private List<String> argNames;
	private UserFunctionArgType userFunctionArgType;
	private List<Expression> functionBody;

	private LambdaExpression(List<String> argNames,
			UserFunctionArgType userFunctionArgType, List<Expression> body) {
		this.argNames = argNames;
		this.userFunctionArgType = userFunctionArgType;
		this.functionBody = body;
	}

	static LambdaExpression createWithConstArgs(List<String> argNames,
			List<Expression> body) {
		return new LambdaExpression(argNames,
				UserFunctionArgType.CONSTANT_ARGS, body);
	}

	static LambdaExpression createWithVarArgs(String argName,
			List<Expression> body) {
		return new LambdaExpression(Collections.singletonList(argName),
				UserFunctionArgType.VARIABLE_ARGS, body);

	}

	public Type evaluate(Scope scope) {
		if (userFunctionArgType.equals(UserFunctionArgType.CONSTANT_ARGS))
			return UserFunctionExpression.createWithConstArgs(functionBody,
					argNames, scope);
		else
			return UserFunctionExpression.createWithVarArgs(functionBody,
					argNames.get(0), scope);
	}

	// This doesn't make sense right now. Will be revisited when we implement
	// quote
	String getPrintValue() {
		return "lambda";
	}
}
