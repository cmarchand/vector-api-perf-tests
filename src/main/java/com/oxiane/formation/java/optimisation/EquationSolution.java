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
}
