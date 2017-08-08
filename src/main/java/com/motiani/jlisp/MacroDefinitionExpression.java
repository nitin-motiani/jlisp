package com.motiani.jlisp;

import java.util.List;
import java.util.stream.Collectors;

final class MacroDefinitionExpression extends ListExpression {

	MacroDefinitionExpression(List<Data> items) {
		super(items);
	}

	// The code is very similar to LambdaExpression code. Try to remove this
	// duplication
	Data evaluate(Scope scope) {
		assert (items.size() >= 3);
		assert (Keywords.DEFMACRO.equals(items.get(0)));

		SymbolExpression variable = (SymbolExpression) items.get(1);
		Data args = items.get(2);

		List<Expression> macroBody = items.stream().skip(3)
				.map(item -> (Expression) item).collect(Collectors.toList());

		Macro macro;
		if (args instanceof ListExpression) {
			List<SymbolExpression> argNames = ((ListExpression) args)
					.getItems().stream().map(arg -> (SymbolExpression) arg)
					.collect(Collectors.toList());
			macro = Macro.createWithConstArgs(macroBody, argNames, scope);
		} else if (args instanceof SymbolExpression) {
			macro = Macro.createWithVarArgs(macroBody, (SymbolExpression) args,
					scope);
		} else {
			throw new IllegalArgumentException("Invalid defmacro expression");
		}

		scope.create(variable, macro);
		return variable;
	}

}
