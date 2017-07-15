package com.motiani.jlisp;

import java.util.List;

// Purpose of this class is to create the correct concrete implementation of ListExpression 
// based on what items there are are in the list
class ListExpressionFactory {
	static ListExpression createListExpression(List<Type> items) {
		// Seems cleaner to do this. We don't really need this as items.get(0)
		// will return null, and that won't be equal to any keyword symbol
		if (items.size() == 0)
			return new ProcedureExpression(items);

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
