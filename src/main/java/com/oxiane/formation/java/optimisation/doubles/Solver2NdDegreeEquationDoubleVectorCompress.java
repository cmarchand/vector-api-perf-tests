package com.oxiane.formation.java.optimisation.doubles;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Solver2NdDegreeEquationDoubleVectorCompress implements Solver2ndDegreeEquationDouble {
  @Override
  public List<EquationSolutionDouble> solve(List<EquationDouble> equations) {
    VectorSpecies<Double> species = DoubleVector.SPECIES_PREFERRED;
    if (equations.size() < species.length())
      return new Solver2NdDegreeEquationDoubleRegular().solve(equations);
    Map<EquationDouble, EquationSolutionDouble> solutions = prepareSolutions(equations);
    double[][] equationsWithDiscriminants = removeEquationsWithoutRealSolutions(equations, species, solutions);
    calculateRoots(equationsWithDiscriminants, species, solutions);
    return generateListInOriginalOrder(equations, solutions);
  }

  private Map<EquationDouble, EquationSolutionDouble> prepareSolutions(List<EquationDouble> equations) {
    ConcurrentHashMap<EquationDouble, EquationSolutionDouble> solutions = new ConcurrentHashMap<>();
    for (EquationDouble equationDouble : equations) {
      solutions.put(equationDouble, new EquationSolutionDouble(equationDouble));
    }
    return solutions;
  }

  private double[][] removeEquationsWithoutRealSolutions(List<EquationDouble> equations, VectorSpecies<Double> species,
                                                         Map<EquationDouble, EquationSolutionDouble> solutions) {
    EquationArrays arrays = equationsIntoArrays(equations);
    double[] as = arrays.as();
    double[] bs = arrays.bs();
    double[] cs = arrays.cs();
    double[] discriminants = new double[cs.length];
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
      DoubleVector D = B
          .mul(B)
          .sub(A
              .mul(4d)
              .mul(C));
      D.intoArray(discriminants, index);
      VectorMask<Double> positiveOrNullDiscriminants = D.compare(VectorOperators.GE, 0d);
      A.compress(positiveOrNullDiscriminants)
       .intoArray(compressedAs, filteredCount);
      B.compress(positiveOrNullDiscriminants)
       .intoArray(compressedBs, filteredCount);
      C.compress(positiveOrNullDiscriminants)
       .intoArray(compressedCs, filteredCount);
      D.compress(positiveOrNullDiscriminants)
       .intoArray(compressedDs, filteredCount);
      filteredCount += positiveOrNullDiscriminants.trueCount();
    }
    for (int index2 = index; index2 < as.length; index2++) {
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
    for (int i = 0; i < as.length; i++) {
      if (discriminants[i] < 0) {
        solutions
            .get(new EquationDouble(as[i], bs[i], cs[i]))
            .setDiscriminant(discriminants[i]);
      }
    }
    return new double[][]{
        reduceSizeOf(compressedAs, filteredCount),
        reduceSizeOf(compressedBs, filteredCount),
        reduceSizeOf(compressedCs, filteredCount),
        reduceSizeOf(compressedDs, filteredCount)
    };
  }

  private static void calculateRoots(
      double[][] equationsWithDiscriminants,
      VectorSpecies<Double> species,
      Map<EquationDouble, EquationSolutionDouble> solutions) {
    double[] as = equationsWithDiscriminants[0];
    double[] bs = equationsWithDiscriminants[1];
    double[] cs = equationsWithDiscriminants[2];
    double[] ds = equationsWithDiscriminants[3];
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
    for (int i = 0; i < as.length; i++) {
      EquationDouble key = new EquationDouble(as[i], bs[i], cs[i]);
      EquationSolutionDouble equationSolutionDouble = solutions.get(key);
      equationSolutionDouble.setDiscriminant(ds[i]);
      equationSolutionDouble.addSolution(roots1[i]);
      equationSolutionDouble.addSolution(roots2[i]);
    }
  }

  private static List<EquationSolutionDouble> generateListInOriginalOrder(
      List<EquationDouble> equationDoubles,
      Map<EquationDouble, EquationSolutionDouble> solutions) {
    List<EquationSolutionDouble> ret = new ArrayList<>(equationDoubles.size());
    for (EquationDouble equationDouble : equationDoubles) {
      ret.add(solutions.get(equationDouble));
    }
    return ret;
  }

  private double[] reduceSizeOf(double[] source, int size) {
    return Arrays.copyOf(source, size);
  }

  private EquationArrays equationsIntoArrays(List<EquationDouble> equationDoubles) {
    double[] as = new double[equationDoubles.size()];
    double[] bs = new double[equationDoubles.size()];
    double[] cs = new double[equationDoubles.size()];
    for (int i = 0; i < equationDoubles.size(); i++) {
      EquationDouble equationDouble = equationDoubles.get(i);
      as[i] = equationDouble.a();
      bs[i] = equationDouble.b();
      cs[i] = equationDouble.c();
    }
    return new EquationArrays(as, bs, cs);
  }

  private record EquationArrays(double[] as, double[] bs, double[] cs) {
  }
}
