package com.motiani.jlisp;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

class Scope {
	private Map<String, Expression> variableMap;
	@Nullable
	private Scope parentScope;

	Scope(Scope parentScope) {
		this.parentScope = parentScope;
		this.variableMap = new HashMap<>();
	}

	Expression get(String variable) {
		if (variableMap.containsKey(variable)) {
			return variableMap.get(variable);
		}

		if (parentScope != null) {
			return parentScope.get(variable);
		}

		throw new RuntimeException("Variable " + variable + " is not defined");
	}

	void create(String variable, Expression expression) {
		// Since scheme allows happily redefining a variable, we just put the
		// expression in map,
		// no need to check if it exists or not.
		variableMap.put(variable, expression);
	}

	void modify(String variable, Expression expression) {
		if (variableMap.containsKey(variable)) {
			variableMap.put(variable, expression);
			return;
		}

		if (parentScope != null) {
			parentScope.modify(variable, expression);
			return;
		}

		throw new RuntimeException("Variable " + variable + " not found.");
	}
}
