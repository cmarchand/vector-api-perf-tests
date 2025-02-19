package com.oxiane.formation.java.optimisation;

import java.util.*;

public class EquationSolution {
  final Equation equation;
  double discriminant;
  final SortedSet<Double> solutions;

  public EquationSolution(Equation equation) {
    this.equation = equation;
    this.solutions = new TreeSet<>();
  }

  public Equation equation() {
    return equation;
  }

  public SortedSet<Double> solutions() {
    return Collections.unmodifiableSortedSet(solutions);
  }

  public double discriminent() {
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
    builder.append("Δ = ").append(discriminent()).append("\n");
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
