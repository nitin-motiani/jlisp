package com.motiani.jlisp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

class NativeFunctions {
	static int PRECISION = 6;

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

				// TODO: Maybe should store these as actual fractions like
				// scheme seems to do, but for some other time.
				BigDecimal result = arg0.divide(product, PRECISION,
						RoundingMode.HALF_EVEN);
				return new NumberExpression(result);
			}
		};
	}

	static FunctionExpression abs() {
		return new FunctionExpression() {
			@Override
			public Expression call(List<Expression> args) {
				if (args.size() != 1) {
					throw new IllegalArgumentException(
							"abs can take exactly one argument");
				}

				if (!(args.get(0) instanceof NumberExpression))
					throw new IllegalArgumentException(
							"abs can take only numeric argument");

				BigDecimal result = ((NumberExpression) args.get(0)).getValue()
						.abs();

				return new NumberExpression(result);
			}
		};
	}

	static FunctionExpression numberEquality() {
		return new FunctionExpression() {
			@Override
			public Expression call(List<Expression> args) {
				Boolean result = args.isEmpty()
						|| args.stream()
								.map(expression -> {
									if (expression instanceof NumberExpression)
										return ((NumberExpression) expression)
												.getValue();
									else
										throw new IllegalArgumentException(
												"Invalid argument "
														+ expression
														+ " for subtraction");
								}).collect(Collectors.toSet()).size() == 1;

				return new BooleanExpression(result);
			}
		};
	}
}
