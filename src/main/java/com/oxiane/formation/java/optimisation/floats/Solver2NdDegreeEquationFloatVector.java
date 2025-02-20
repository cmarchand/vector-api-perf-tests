package com.oxiane.formation.java.optimisation.floats;

import jdk.incubator.vector.*;

import java.util.List;

public class Solver2NdDegreeEquationFloatVector implements Solver2ndDegreeEquationFloat {

  public List<EquationSolutionFloat> solve(List<EquationFloat> equations) {
    VectorSpecies<Float> species = FloatVector.SPECIES_PREFERRED;
    if(equations.size() < species.length())
      return new Solver2NdDegreeEquationFloatRegular().solve(equations);
    float[] as = new float[equations.size()];
    float[] bs = new float[equations.size()];
    float[] cs = new float[equations.size()];
    EquationSolutionFloat[] solutions = new EquationSolutionFloat[equations.size()];
    for (int i = 0; i < equations.size(); i++) {
      EquationFloat eq = equations.get(i);
      as[i] = eq.a();
      bs[i] = eq.b();
      cs[i] = eq.c();
      solutions[i] = new EquationSolutionFloat(eq);
    }
    float[] discriminants = new float[as.length];
    float[] roots1 = new float[as.length];
    float[] roots2 = new float[as.length];
    int index = 0;
    for(; index < species.loopBound(as.length); index += species.length()) {
      FloatVector A = FloatVector.fromArray(species, as, index);
      FloatVector B = FloatVector.fromArray(species, bs, index);
      FloatVector C = FloatVector.fromArray(species, cs, index);
      FloatVector D = B.mul(B) .sub(A.mul(C).mul(4f));
      D.intoArray(discriminants, index);
      // traitement des discriminants positifs
      VectorMask<Float> mask = D.compare(VectorOperators.GE, 0f);
      FloatVector sqrt = D.lanewise(VectorOperators.SQRT, mask);
      B
          .mul(-1f, mask)
          .add(sqrt.mul(-1f))
          .div(2f)
          .div(A, mask)
          .intoArray(roots1, index, mask);
      B
          .mul(-1f, mask)
          .add(sqrt)
          .div(2f)
          .div(A, mask)
          .intoArray(roots2, index, mask);
    }
    for(int index2 = index; index2 < as.length ; index2++) {
      discriminants[index2] = bs[index2] * bs[index2] - (4 * as[index2] * cs[index2]);
      if(discriminants[index2] >= 0) {
        float sqrt = (float)Math.sqrt(discriminants[index2]);
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