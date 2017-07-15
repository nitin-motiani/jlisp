package com.motiani.jlisp;

import java.util.List;

final class DefinitionExpression extends ListExpression {

	DefinitionExpression(List<Type> items) {
		super(items);
	}

	public Type evaluate(Scope scope) {
		assert (items.size() == 3);
		assert (Keywords.DEFINE.equals(items.get(0)));

		SymbolExpression variable = (SymbolExpression) items.get(1);
		Expression expression = (Expression) items.get(2);
		scope.create(variable, expression.evaluate(scope));
		return variable;
	}
}
