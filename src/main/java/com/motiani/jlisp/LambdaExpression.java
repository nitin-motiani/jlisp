package com.motiani.jlisp;

import java.util.List;
import java.util.stream.Collectors;

final class LambdaExpression extends ListExpression {

	LambdaExpression(List<Data> items) {
		super(items);
	}

	Data evaluate(Scope scope) {
		assert (items.size() >= 3);
		assert (Keywords.LAMBDA.equals(items.get(0)));

		Data args = items.get(1);
		List<Expression> functionBody = items.stream().skip(2)
				.map(item -> (Expression) item).collect(Collectors.toList());

		if (args instanceof ListExpression) {
			List<SymbolExpression> argNames = ((ListExpression) args)
					.getItems().stream().map(arg -> (SymbolExpression) arg)
					.collect(Collectors.toList());
			return UserFunctionExpression.createWithConstArgs(functionBody,
					argNames, scope);
		} else if (args instanceof SymbolExpression) {
			return UserFunctionExpression.createWithVarArgs(functionBody,
					(SymbolExpression) args, scope);
		} else {
			throw new IllegalArgumentException("Invalid lambda expression");
		}
	}
}
