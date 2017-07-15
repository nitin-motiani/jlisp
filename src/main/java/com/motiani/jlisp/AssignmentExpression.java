package com.motiani.jlisp;

import java.util.List;

final class AssignmentExpression extends ListExpression {

	AssignmentExpression(List<Type> items) {
		super(items);
	}

	Type evaluate(Scope scope) {
		assert (items.size() == 3);
		assert (Keywords.ASSIGN.equals(items.get(0)));

		SymbolExpression variable = (SymbolExpression) (items.get(1));
		Expression expression = (Expression) (items.get(2));
		Type previousValue = variable.evaluate(scope);
		scope.modify(variable, expression.evaluate(scope));
		return previousValue;
	}
}
