package com.motiani.jlisp;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

class Scope {
	private Map<SymbolExpression, Data> variableMap;
	@Nullable
	private Scope parentScope;

	Scope(Scope parentScope) {
		this.parentScope = parentScope;
		this.variableMap = new HashMap<>();
	}

	Data get(SymbolExpression variable) {
		if (variableMap.containsKey(variable)) {
			return variableMap.get(variable);
		}

		if (parentScope != null) {
			return parentScope.get(variable);
		}

		throw new RuntimeException("Variable " + variable.getDisplayValue()
				+ " is not defined");
	}

	void create(SymbolExpression variable, Data data) {
		// Since scheme allows happily redefining a variable, we just put the
		// expression in map,
		// no need to check if it exists or not.
		variableMap.put(variable, data);
	}

	void modify(SymbolExpression variable, Data data) {
		if (variableMap.containsKey(variable)) {
			variableMap.put(variable, data);
			return;
		}

		if (parentScope != null) {
			parentScope.modify(variable, data);
			return;
		}

		throw new RuntimeException("Variable " + variable.getDisplayValue()
				+ " not found.");
	}
}
