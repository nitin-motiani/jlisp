package com.motiani.jlisp;

final class SymbolExpression extends Expression {
	private String symbol;

	SymbolExpression(String symbol) {
		this.symbol = symbol;
	}

	public Type evaluate(Scope scope) {
		return scope.get(symbol);
	}

	// This doesn't make sense right now. Will be revisited when we implement
	// quote
	String getPrintValue() {
		return symbol;
	}

	// Implement hashCode and equals as the plan is to use SymbolExpression as
	// key in scope. So it must have proper hashcode and equals implementation
	// TODO: Make symbol objects interned
	@Override
	public int hashCode() {
		// TODO: Do we need null check?
		return symbol.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SymbolExpression other = (SymbolExpression) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

}
