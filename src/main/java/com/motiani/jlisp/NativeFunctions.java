package com.motiani.jlisp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO: Does it make sense to implement these functions in their respective classes and only have wrappers here? 
// For example, have all arithmetic stuff in NumberExpression, and car in ListExpression
class NativeFunctions {
	static int PRECISION = 6;

	static Function addition() {
		return new Function() {
			@Override
			public Type call(List<Type> args) {
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

	static Function subtraction() {
		return new Function() {
			@Override
			public Type call(List<Type> args) {
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

	static Function mutliplication() {
		return new Function() {
			@Override
			public Type call(List<Type> args) {
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

	static Function division() {
		return new Function() {

			@Override
			public Type call(List<Type> args) {
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

	static Function abs() {
		return new Function() {
			@Override
			public Type call(List<Type> args) {
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

	static Function numberEquality() {
		return new Function() {
			@Override
			public Type call(List<Type> args) {
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

	static Function car() {
		return new Function() {

			@Override
			public Type call(List<Type> args) {
				if (args.size() != 1) {
					throw new IllegalArgumentException(
							"car can take exactly one argument");
				}

				if (!(args.get(0) instanceof ListExpression))
					throw new IllegalArgumentException(
							"car can take only list argument");

				return ((ListExpression) args.get(0)).getItems().get(0);
			}
		};

	}

	static Function cdr() {
		return new Function() {

			@Override
			public Type call(List<Type> args) {
				if (args.size() != 1) {
					throw new IllegalArgumentException(
							"cdr can take exactly one argument");
				}

				if (!(args.get(0) instanceof ListExpression))
					throw new IllegalArgumentException(
							"cdr can take only list argument");

				List<Type> cdrExprs = ((ListExpression) args.get(0)).getItems()
						.stream().skip(1).collect(Collectors.toList());

				return new ListExpression(cdrExprs);
			}
		};

	}

	static Function list() {
		return new Function() {

			@Override
			public Type call(List<Type> args) {
				return new ListExpression(args);
			}
		};
	}

	static Function map() {
		return new Function() {

			@Override
			public Type call(List<Type> args) {
				if (args.size() < 2)
					throw new IllegalArgumentException(
							"map needs at least 2 arguments");
				if (!(args.get(0) instanceof Callable))
					throw new IllegalArgumentException(
							"Need to provide a callable as first argument");

				int resultSize = args
						.stream()
						.skip(1)
						.map(arg -> {
							if (!(arg instanceof ListExpression)) {
								throw new IllegalArgumentException(
										"Invalid argument " + arg.toString()
												+ " for map. Should be a list");
							}

							return ((ListExpression) arg).getItems().size();
						}).min(Integer::compare).get();

				Callable callable = (Callable) args.get(0);
				int numArgs = args.size() - 1;
				List<Type> resultExpressions = new ArrayList<>(resultSize);
				for (int i = 0; i < resultSize; i++) {
					List<Type> callableArgs = new ArrayList<>(numArgs);
					// Start with 1 as first arg is the callable
					for (int j = 1; j <= numArgs; j++) {
						callableArgs.add(((ListExpression) args.get(j))
								.getItems().get(i));
					}

					resultExpressions.add(callable.call(callableArgs));
				}

				return new ListExpression(resultExpressions);
			}
		};
	}
}
