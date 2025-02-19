package com.oxiane.formation.java.optimisation;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Solver2ndDegreeEquationVectorCompress implements Solver2ndDegreeEquation {
  @Override
  public List<EquationSolution> solve(List<Equation> equations) {
    VectorSpecies<Double> species = DoubleVector.SPECIES_PREFERRED;
    if (equations.size() < species.length())
      return new Solver2ndDegreeEquationRegular().solve(equations);
    Map<Equation, EquationSolution> solutions = prepareSolutions(equations);
    double[][] equationsWithDiscriminants = removeEquationsWithoutRealSolutions(equations, species, solutions);
    extractSolutions(equationsWithDiscriminants, species, solutions);
    return generateListInOriginalOrder(equations, solutions);
  }

  private Map<Equation, EquationSolution> prepareSolutions(List<Equation> equations) {
    ConcurrentHashMap<Equation, EquationSolution> solutions = new ConcurrentHashMap<>();
    for (Equation equation : equations) {
      solutions.put(equation, new EquationSolution(equation));
    }
    return solutions;
  }

  private double[][] removeEquationsWithoutRealSolutions(List<Equation> equations, VectorSpecies<Double> species, Map<Equation, EquationSolution> solutions) {
    EquationArrays arrays = equationsIntoArrays(equations);
    double[] as = arrays.as();
    double[] bs = arrays.bs();
    double[] cs = arrays.cs();
    double[] discriminants = new double[cs.length];
    // calculate discriminant and filter equations that have solution(s)
    int index = 0;
    int filteredCount = 0;
    double[] compressedAs = new double[cs.length];
    double[] compressedBs = new double[cs.length];
    double[] compressedCs = new double[cs.length];
    double[] compressedDs = new double[cs.length];
    for (; index < species.loopBound(as.length); index += species.length()) {
      DoubleVector A = DoubleVector.fromArray(species, as, index);
      DoubleVector B = DoubleVector.fromArray(species, bs, index);
      DoubleVector C = DoubleVector.fromArray(species, cs, index);
      DoubleVector DISCRIMINANT = B
          .mul(B)
          .sub(A
              .mul(4d)
              .mul(C));
      DISCRIMINANT.intoArray(discriminants, index);
      VectorMask<Double> positiveOrNullDiscriminants = DISCRIMINANT.compare(VectorOperators.GE, 0d);
      A.compress(positiveOrNullDiscriminants)
       .intoArray(compressedAs, filteredCount);
      B.compress(positiveOrNullDiscriminants)
       .intoArray(compressedBs, filteredCount);
      C.compress(positiveOrNullDiscriminants)
       .intoArray(compressedCs, filteredCount);
      DISCRIMINANT.compress(positiveOrNullDiscriminants)
                  .intoArray(compressedDs, filteredCount);
      filteredCount += positiveOrNullDiscriminants.trueCount();
    }
    for (int index2 = index; index2 < as.length; index2++) {
      System.out.println("passage dans correctif de longueur");
      double discriminant = bs[index2] * bs[index2] - (4 * as[index2] * cs[index2]);
      if (discriminant >= 0) {
        compressedAs[filteredCount] = as[index2];
        compressedBs[filteredCount] = bs[index2];
        compressedCs[filteredCount] = cs[index2];
        compressedDs[filteredCount] = discriminant;
        filteredCount++;
      }
      discriminants[index2] = discriminant;
    }
    // on affecte les discriminants dans les solutions pour les discriminants négatifs
    for (int i = 0; i < as.length; i++) {
      if(discriminants[i] < 0) {
        solutions
            .get(new Equation(as[i], bs[i], cs[i]))
            .setDiscriminent(discriminants[i]);
      }
    }
    return new double[][]{
        reduceSizeOf(compressedAs, filteredCount),
        reduceSizeOf(compressedBs, filteredCount),
        reduceSizeOf(compressedCs, filteredCount),
        reduceSizeOf(compressedDs, filteredCount)
    };
  }

  private static void extractSolutions(double[][] equationsWithDiscriminants, VectorSpecies<Double> species,
                                       Map<Equation, EquationSolution> solutions) {
    double[] as = equationsWithDiscriminants[0];
    double[] bs = equationsWithDiscriminants[1];
    double[] cs = equationsWithDiscriminants[2];
    double[] ds = equationsWithDiscriminants[3];
    // deuxieme partie, calcul des racines
    double[] roots1 = new double[as.length];
    double[] roots2 = new double[as.length];
    int index = 0;
    for (; index < species.loopBound(as.length); index += species.length()) {
      DoubleVector A = DoubleVector.fromArray(species, as, index);
      DoubleVector B = DoubleVector.fromArray(species, bs, index);
      DoubleVector D = DoubleVector.fromArray(species, ds, index);
      DoubleVector sqrt = D.lanewise(VectorOperators.SQRT);
      B.mul(-1d)
       .add(sqrt.mul(-1d))
       .div(2d)
       .div(A)
       .intoArray(roots1, index);
      B.mul(-1d)
       .add(sqrt)
       .div(2d)
       .div(A)
       .intoArray(roots2, index);
    }
    for (int index2 = index; index2 < as.length; index2++) {
      double sqrt = Math.sqrt(ds[index2]);
      double denominator = 2 * as[index2];
      roots1[index2] = (-bs[index2] - sqrt) / denominator;
      roots2[index2] = (-bs[index2] + sqrt) / denominator;
    }
    // on remet les solutions
    for (int i = 0; i < as.length; i++) {
      Equation key = new Equation(as[i], bs[i], cs[i]);
      EquationSolution equationSolution = solutions.get(key);
      equationSolution.setDiscriminent(ds[i]);
      equationSolution.addSolution(roots1[i]);
      equationSolution.addSolution(roots2[i]);
    }
  }

  private static List<EquationSolution> generateListInOriginalOrder(List<Equation> equations, Map<Equation,
      EquationSolution> solutions) {
    // pour remettre les choses dans le bon ordre
    List<EquationSolution> ret = new ArrayList<>(equations.size());
    for (Equation equation : equations) {
      ret.add(solutions.get(equation));
    }
    return ret;
  }

  private double[] reduceSizeOf(double[] source, int size) {
    return Arrays.copyOf(source, size);
  }

  private EquationArrays equationsIntoArrays(List<Equation> equations) {
    double[] as = new double[equations.size()];
    double[] bs = new double[equations.size()];
    double[] cs = new double[equations.size()];
    for (int i = 0; i < equations.size(); i++) {
      Equation equation = equations.get(i);
      as[i] = equation.a();
      bs[i] = equation.b();
      cs[i] = equation.c();
    }
    return new EquationArrays(as, bs, cs);
  }

  private record EquationArrays(double[] as, double[] bs, double[] cs) {
  }
}
