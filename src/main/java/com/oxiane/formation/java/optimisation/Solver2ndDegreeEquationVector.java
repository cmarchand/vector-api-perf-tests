package com.oxiane.formation.java.optimisation;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import java.util.List;

public class Solver2ndDegreeEquationVector implements Solver2ndDegreeEquation {

  public List<EquationSolution> solve(List<Equation> equations) {
    VectorSpecies<Double> species = DoubleVector.SPECIES_PREFERRED;
    if(equations.size() < species.length())
      return new Solver2ndDegreeEquationRegular().solve(equations);
    double[] as = new double[equations.size()];
    double[] bs = new double[equations.size()];
    double[] cs = new double[equations.size()];
    EquationSolution[] solutions = new EquationSolution[equations.size()];
    for (int i = 0; i < equations.size(); i++) {
      Equation equation = equations.get(i);
      as[i] = equation.a();
      bs[i] = equation.b();
      cs[i] = equation.c();
      solutions[i] = new EquationSolution(equation);
    }
    int index = 0;
    double[] discriminents = new double[as.length];
    double[] roots1 = new double[as.length];
    double[] roots2 = new double[as.length];
    for(; index < species.loopBound(as.length); index += species.length()) {
      DoubleVector A = DoubleVector.fromArray(species, as, index);
      DoubleVector B = DoubleVector.fromArray(species, bs, index);
      DoubleVector C = DoubleVector.fromArray(species, cs, index);
      DoubleVector squareB = B.mul(B);
      DoubleVector ac4 = A.mul(4d)
                          .mul(C);
      DoubleVector DISCRIMINENT = squareB.sub(ac4);
      DISCRIMINENT.intoArray(discriminents, index);
      // traitement des discriminents 0
      VectorMask<Double> nullDiscriminents = DISCRIMINENT.compare(VectorOperators.EQ, 0d);
      DoubleVector ROOT1 = B
          .mul(-1d, nullDiscriminents)
          .div(2d)
          .div(A, nullDiscriminents);
      ROOT1.intoArray(roots1, index, nullDiscriminents);
      // traitement des discriminents positifs
      VectorMask<Double> positiveDiscriminents = DISCRIMINENT.compare(VectorOperators.GT, 0d);
      DoubleVector sqrt = DISCRIMINENT.lanewise(VectorOperators.SQRT, positiveDiscriminents);
      B
          .mul(-1d, positiveDiscriminents)
          .add(sqrt.mul(-1d))
          .div(2d)
          .div(A, positiveDiscriminents)
          .intoArray(roots1, index, positiveDiscriminents);
      B
          .mul(-1d, positiveDiscriminents)
          .add(sqrt)
          .div(2d)
          .div(A, positiveDiscriminents)
          .intoArray(roots2, index, positiveDiscriminents);
    }
    for(int index2 = index; index2 < as.length ; index2++) {
      discriminents[index2] = bs[index2] * bs[index2] - (4 * as[index2] * cs[index2]);
      if(discriminents[index2] == 0) {
        // cas des discriminents 0
        roots1[index2] = (- bs[index2])/(2 + as[index2]);
      } else if(discriminents[index2] > 0) {
        // cas des discriminents > 0
        double sqrt = Math.sqrt(discriminents[index2]);
        roots1[index2] = (- bs[index2] - sqrt)/(2 + as[index2]);
        roots2[index2] = (- bs[index2] + sqrt)/(2 + as[index2]);
      }
    }
    for (int i = 0; i < discriminents.length; i++) {
      solutions[i].setDiscriminent(discriminents[i]);
      if(discriminents[i] >= 0) {
        solutions[i].addSolution(roots1[i]);
      }
      if(discriminents[i] > 0) {
        solutions[i].addSolution(roots2[i]);
      }
    }
    return List.of(solutions);
  }
}
