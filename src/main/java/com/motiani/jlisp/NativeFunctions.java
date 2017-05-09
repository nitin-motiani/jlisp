package com.motiani.jlisp;

import java.math.BigDecimal;
import java.util.List;

class NativeFunctions {
	static FunctionExpression addition() {
		return new FunctionExpression() {
			@Override
			public Expression call(List<Expression> args) {
				BigDecimal sum = args
						.stream()
						.map(arg -> {
							if (arg instanceof NumberExpression)
								return ((NumberExpression) arg).getValue();
							else
								throw new IllegalArgumentException(
										"Invalid argument " + arg
												+ " to addition");
						}).reduce(BigDecimal.ZERO, BigDecimal::add);
				return new NumberExpression(sum);
			}
		};
	}

	static FunctionExpression subtraction() {
		return new FunctionExpression() {
			@Override
			public Expression call(List<Expression> args) {
				if (args.size() == 0)
					throw new IllegalArgumentException(
							"At least one argument is required for -");

				if (!(args.get(0) instanceof NumberExpression))
					throw new IllegalArgumentException("Invalid argument "
							+ args.get(0) + " to subtraction");

				BigDecimal arg0 = ((NumberExpression) args.get(0)).getValue();
				if (args.size() == 1) {
					return new NumberExpression(BigDecimal.ZERO.subtract(arg0));
				}

				// TODO: Should we use addition function code here
				BigDecimal sum = args
						.stream()
						.skip(1)
						.map(expression -> {
							if (expression instanceof NumberExpression)
								return ((NumberExpression) expression)
										.getValue();
							else
								throw new IllegalArgumentException(
										"Invalid argument " + expression
												+ " for subtraction");
						}).reduce(BigDecimal.ZERO, BigDecimal::add);

				BigDecimal result = arg0.subtract(sum);
				return new NumberExpression(result);
			}
		};
	}

	static FunctionExpression mutliplication() {
		return new FunctionExpression() {
			@Override
			public Expression call(List<Expression> args) {
				BigDecimal product = args
						.stream()
						.map(arg -> {
							if (arg instanceof NumberExpression)
								return ((NumberExpression) arg).getValue();
							else
								throw new IllegalArgumentException(
										"Invalid argument " + arg
												+ " to addition");
						}).reduce(BigDecimal.ONE, BigDecimal::multiply);
				return new NumberExpression(product);
			}
		};
	}

	static FunctionExpression division() {
		return new FunctionExpression() {

			@Override
			public Expression call(List<Expression> args) {
				if (args.size() == 0)
					throw new IllegalArgumentException(
							"At least one argument is required for -");

				if (!(args.get(0) instanceof NumberExpression))
					throw new IllegalArgumentException("Invalid argument "
							+ args.get(0) + " to subtraction");

				BigDecimal arg0 = ((NumberExpression) args.get(0)).getValue();
				if (args.size() == 1) {
					return new NumberExpression(BigDecimal.ONE.divide(arg0));
				}

				// TODO: Should we use multiplication function code here
				BigDecimal product = args
						.stream()
						.skip(1)
						.map(expression -> {
							if (expression instanceof NumberExpression)
								return ((NumberExpression) expression)
										.getValue();
							else
								throw new IllegalArgumentException(
										"Invalid argument " + expression
												+ " for subtraction");
						}).reduce(BigDecimal.ONE, BigDecimal::multiply);

				BigDecimal result = arg0.divide(product);
				return new NumberExpression(result);
			}
		};
	}
}
