package com.oxiane.formation.java.optimisation.doubles;

import java.util.*;

public class EquationSolutionDouble {
  final EquationDouble equationDouble;
  double discriminant;
  final SortedSet<Double> solutions;

  public EquationSolutionDouble(EquationDouble equationDouble) {
    this.equationDouble = equationDouble;
    this.solutions = new TreeSet<>();
  }

  public EquationDouble equation() {
    return equationDouble;
  }

  public SortedSet<Double> solutions() {
    return Collections.unmodifiableSortedSet(solutions);
  }

  public double discriminant() {
    return discriminant;
  }

  public void setDiscriminant(double discriminant) {
    this.discriminant = discriminant;
  }

  public void addSolution(double d) {
    solutions.add(d);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(equation().a()).append("x² + ");
    builder.append(equation().b()).append("x + ");
    builder.append(equation().c()).append(" = 0\n");
    builder.append("Δ = ").append(discriminant()).append("\n");
    int i = 0;
    for (Double solution : solutions()) {
      builder
          .append("R")
          .append(i++)
          .append(": ")
          .append(solution)
          .append("\n");
    }
    return builder.toString();
  }
}
