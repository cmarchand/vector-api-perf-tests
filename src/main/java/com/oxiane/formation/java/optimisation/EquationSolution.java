package com.oxiane.formation.java.optimisation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EquationSolution {
  final Equation equation;
  double discriminent;
  final List<Double> solutions;

  public EquationSolution(Equation equation) {
    this.equation = equation;
    this.solutions = new ArrayList<>();
  }

  public Equation equation() {
    return equation;
  }

  public List<Double> solutions() {
    return Collections.unmodifiableList(solutions);
  }

  public double discriminent() {
    return discriminent;
  }

  public void setDiscriminent(double discriminent) {
    this.discriminent = discriminent;
  }

  public void addSolution(double d) {
    solutions.add(d);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(equation().a()).append("x^2 + ");
    builder.append(equation().b()).append("x + ");
    builder.append(equation().c()).append(" = 0\n");
    builder.append("Δ = ").append(discriminent()).append("\n");
    for (int i = 0; i < solutions().size(); i++) {
      builder.append("R").append(i).append(" - ").append(solutions().get(i)).append("\n");
    }
    return builder.toString();
  }
}
