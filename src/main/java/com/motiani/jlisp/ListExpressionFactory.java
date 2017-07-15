package com.motiani.jlisp;

import java.util.ArrayList;
import java.util.List;

// Purpose of this class is to create the correct concrete implementation of ListExpression 
// based on what items there are are in the list
class ListExpressionFactory {

	// TODO: I am not 100% sure of this approach. But it seemed a clean way to
	// have one singleton empty list which just returns itself on evaluation.
	// This is basically the nil of the language
	private static ListExpression emptyList = new ListExpression(
			new ArrayList<Type>()) {

		@Override
		public Type evaluate(Scope scope) {
			return this;
		}
	};

	static ListExpression createListExpression(List<Type> items) {
		if (items.size() == 0)
			return emptyList;

		Type symbol = items.get(0);

		if (Keywords.IF.equals(symbol)) {
			return new ConditionalExpression(items);
		} else if (Keywords.DEFINE.equals(symbol)) {
			return new DefinitionExpression(items);
		} else if (Keywords.ASSIGN.equals(symbol)) {
			return new AssignmentExpression(items);
		} else if (Keywords.LAMBDA.equals(symbol)) {
			return new LambdaExpression(items);
		} else if (Keywords.QUOTE.equals(symbol)) {
			return new QuoteExpression(items);
		} else {
			return new ProcedureExpression(items);
		}
	}
}
