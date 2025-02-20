package com.oxiane.formation.java.optimisation.floats;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class EquationSolutionFloat {
  final EquationFloat equationDouble;
  float discriminant;
  final SortedSet<Float> solutions;

  public EquationSolutionFloat(EquationFloat equationDouble) {
    this.equationDouble = equationDouble;
    this.solutions = new TreeSet<>();
  }

  public EquationFloat equation() {
    return equationDouble;
  }

  public SortedSet<Float> solutions() {
    return Collections.unmodifiableSortedSet(solutions);
  }

  public double discriminant() {
    return discriminant;
  }

  public void setDiscriminant(float discriminant) {
    this.discriminant = discriminant;
  }

  public void addSolution(float d) {
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
    for (Float solution : solutions()) {
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
