package com.oxiane.formation.java.optimisation.doubles;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import java.util.List;

public class Solver2NdDegreeEquationDoubleVector implements Solver2ndDegreeEquationDouble {

  public List<EquationSolutionDouble> solve(List<EquationDouble> equations) {
    VectorSpecies<Double> species = DoubleVector.SPECIES_PREFERRED;
    if(equations.size() < species.length())
      return new Solver2NdDegreeEquationDoubleRegular().solve(equations);
    double[] as = new double[equations.size()];
    double[] bs = new double[equations.size()];
    double[] cs = new double[equations.size()];
    EquationSolutionDouble[] solutions = new EquationSolutionDouble[equations.size()];
    for (int i = 0; i < equations.size(); i++) {
      EquationDouble eq = equations.get(i);
      as[i] = eq.a();
      bs[i] = eq.b();
      cs[i] = eq.c();
      solutions[i] = new EquationSolutionDouble(eq);
    }
    double[] discriminants = new double[as.length];
    double[] roots1 = new double[as.length];
    double[] roots2 = new double[as.length];
    int index = 0;
    for(; index < species.loopBound(as.length); index += species.length()) {
      DoubleVector A = DoubleVector.fromArray(species, as, index);
      DoubleVector B = DoubleVector.fromArray(species, bs, index);
      DoubleVector C = DoubleVector.fromArray(species, cs, index);
      DoubleVector D = B.mul(B) .sub(A.mul(C).mul(4d));
      D.intoArray(discriminants, index);
      // traitement des discriminants positifs
      VectorMask<Double> mask = D.compare(VectorOperators.GE, 0d);
      DoubleVector sqrt = D.lanewise(VectorOperators.SQRT, mask);
      B
          .mul(-1d, mask)
          .add(sqrt.mul(-1d))
          .div(2d)
          .div(A, mask)
          .intoArray(roots1, index, mask);
      B
          .mul(-1d, mask)
          .add(sqrt)
          .div(2d)
          .div(A, mask)
          .intoArray(roots2, index, mask);
    }
    for(int index2 = index; index2 < as.length ; index2++) {
      discriminants[index2] = bs[index2] * bs[index2] - (4 * as[index2] * cs[index2]);
      if(discriminants[index2] >= 0) {
        double sqrt = Math.sqrt(discriminants[index2]);
        roots1[index2] = (- bs[index2] - sqrt)/(2 * as[index2]);
        roots2[index2] = (- bs[index2] + sqrt)/(2 * as[index2]);
      }
    }
    for (int i = 0; i < discriminants.length; i++) {
      solutions[i].setDiscriminant(discriminants[i]);
      if(discriminants[i] >= 0) {
        solutions[i].addSolution(roots1[i]);
        solutions[i].addSolution(roots2[i]);
      }
    }
    return List.of(solutions);
  }
}