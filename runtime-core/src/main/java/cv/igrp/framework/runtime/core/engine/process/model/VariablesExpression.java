package cv.igrp.framework.runtime.core.engine.process.model;


import java.util.Objects;

public class VariablesExpression {

  private final String name;
  private final VariablesOperator operator;
  private final Object value;

	public VariablesExpression(String name,
							   VariablesOperator operator,
							   Object value) {
		this.name = name;
		this.operator = operator;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public VariablesOperator getOperator() {
		return operator;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		VariablesExpression that = (VariablesExpression) o;
		return Objects.equals(name, that.name) && operator == that.operator && Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, operator, value);
	}

	@Override
	public String toString() {
		return "VariablesExpression{" +
				"name='" + name + '\'' +
				", operator=" + operator +
				", value=" + value +
				'}';
	}

}
