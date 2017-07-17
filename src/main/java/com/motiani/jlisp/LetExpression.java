package com.motiani.jlisp;

import java.util.List;
import java.util.stream.Collectors;

final class LetExpression extends ListExpression {

	LetExpression(List<Data> items) {
		super(items);
	}

	private Scope createScope(ListExpression args, Scope parentScope) {
		Scope scope = new Scope(parentScope);
		List<Data> bindings = args.getItems();
		for (Data binding : bindings) {
			if (!(binding instanceof ListExpression)) {
				throw new IllegalArgumentException("Ill formed let expression");
			}

			List<Data> bindingData = ((ListExpression) binding).getItems();
			assert (bindingData.size() == 2);
			SymbolExpression symbol = (SymbolExpression) bindingData.get(0);
			// The evaluation for the value will always be in parent scope. So
			// if x is defined in parent scope, and a local x is defined in let,
			// and y uses x in let binding, the x used will be from parent scope
			Data value = ((Expression) bindingData.get(1))
					.evaluate(parentScope);

			scope.create(symbol, value);
		}

		return scope;
	}

	Data evaluate(Scope scope) {
		assert (items.size() >= 3);
		assert (Keywords.LET.equals(items.get(0)));

		ListExpression args = (ListExpression) items.get(1);
		Scope letScope = createScope(args, scope);

		Data solution = null;
		List<Expression> body = items.stream().skip(2)
				.map(item -> (Expression) item).collect(Collectors.toList());

		for (Expression expr : body) {
			solution = expr.evaluate(letScope);
		}

		return solution;
	}

}
