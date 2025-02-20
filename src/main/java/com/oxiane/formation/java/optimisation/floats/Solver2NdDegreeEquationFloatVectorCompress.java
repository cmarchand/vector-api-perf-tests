package com.oxiane.formation.java.optimisation.floats;

import jdk.incubator.vector.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Solver2NdDegreeEquationFloatVectorCompress implements Solver2ndDegreeEquationFloat {
  @Override
  public List<EquationSolutionFloat> solve(List<EquationFloat> equations) {
    VectorSpecies<Float> species = FloatVector.SPECIES_PREFERRED;
    if (equations.size() < species.length())
      return new Solver2NdDegreeEquationFloatRegular().solve(equations);
    Map<EquationFloat, EquationSolutionFloat> solutions = prepareSolutions(equations);
    float[][] equationsWithDiscriminants = removeEquationsWithoutRealSolutions(equations, species, solutions);
    calculateRoots(equationsWithDiscriminants, species, solutions);
    return generateListInOriginalOrder(equations, solutions);
  }

  private Map<EquationFloat, EquationSolutionFloat> prepareSolutions(List<EquationFloat> equations) {
    ConcurrentHashMap<EquationFloat, EquationSolutionFloat> solutions = new ConcurrentHashMap<>();
    for (EquationFloat equationFloat : equations) {
      solutions.put(equationFloat, new EquationSolutionFloat(equationFloat));
    }
    return solutions;
  }

  private float[][] removeEquationsWithoutRealSolutions(List<EquationFloat> equations, VectorSpecies<Float> species,
                                                         Map<EquationFloat, EquationSolutionFloat> solutions) {
    EquationArrays arrays = equationsIntoArrays(equations);
    float[] as = arrays.as();
    float[] bs = arrays.bs();
    float[] cs = arrays.cs();
    float[] discriminants = new float[cs.length];
    int index = 0;
    int filteredCount = 0;
    float[] compressedAs = new float[cs.length];
    float[] compressedBs = new float[cs.length];
    float[] compressedCs = new float[cs.length];
    float[] compressedDs = new float[cs.length];
    for (; index < species.loopBound(as.length); index += species.length()) {
      FloatVector A = FloatVector.fromArray(species, as, index);
      FloatVector B = FloatVector.fromArray(species, bs, index);
      FloatVector C = FloatVector.fromArray(species, cs, index);
      FloatVector D = B
          .mul(B)
          .sub(A
              .mul(4f)
              .mul(C));
      D.intoArray(discriminants, index);
      VectorMask<Float> positiveOrNullDiscriminants = D.compare(VectorOperators.GE, 0f);
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
      float discriminant = bs[index2] * bs[index2] - (4f * as[index2] * cs[index2]);
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
            .get(new EquationFloat(as[i], bs[i], cs[i]))
            .setDiscriminant(discriminants[i]);
      }
    }
    return new float[][]{
        reduceSizeOf(compressedAs, filteredCount),
        reduceSizeOf(compressedBs, filteredCount),
        reduceSizeOf(compressedCs, filteredCount),
        reduceSizeOf(compressedDs, filteredCount)
    };
  }

  private static void calculateRoots(
      float[][] equationsWithDiscriminants,
      VectorSpecies<Float> species,
      Map<EquationFloat, EquationSolutionFloat> solutions) {
    float[] as = equationsWithDiscriminants[0];
    float[] bs = equationsWithDiscriminants[1];
    float[] cs = equationsWithDiscriminants[2];
    float[] ds = equationsWithDiscriminants[3];
    float[] roots1 = new float[as.length];
    float[] roots2 = new float[as.length];
    int index = 0;
    for (; index < species.loopBound(as.length); index += species.length()) {
      FloatVector A = FloatVector.fromArray(species, as, index);
      FloatVector B = FloatVector.fromArray(species, bs, index);
      FloatVector D = FloatVector.fromArray(species, ds, index);
      FloatVector sqrt = D.lanewise(VectorOperators.SQRT);
      B.mul(-1f)
       .add(sqrt.mul(-1f))
       .div(2f)
       .div(A)
       .intoArray(roots1, index);
      B.mul(-1f)
       .add(sqrt)
       .div(2f)
       .div(A)
       .intoArray(roots2, index);
    }
    for (int index2 = index; index2 < as.length; index2++) {
      float sqrt = (float)Math.sqrt(ds[index2]);
      float denominator = 2 * as[index2];
      roots1[index2] = (-bs[index2] - sqrt) / denominator;
      roots2[index2] = (-bs[index2] + sqrt) / denominator;
    }
    for (int i = 0; i < as.length; i++) {
      EquationFloat key = new EquationFloat(as[i], bs[i], cs[i]);
      EquationSolutionFloat equationSolutionFloat = solutions.get(key);
      equationSolutionFloat.setDiscriminant(ds[i]);
      equationSolutionFloat.addSolution(roots1[i]);
      equationSolutionFloat.addSolution(roots2[i]);
    }
  }

  private static List<EquationSolutionFloat> generateListInOriginalOrder(
      List<EquationFloat> equationFloats,
      Map<EquationFloat, EquationSolutionFloat> solutions) {
    List<EquationSolutionFloat> ret = new ArrayList<>(equationFloats.size());
    for (EquationFloat equationFloat : equationFloats) {
      ret.add(solutions.get(equationFloat));
    }
    return ret;
  }

  private float[] reduceSizeOf(float[] source, int size) {
    return Arrays.copyOf(source, size);
  }

  private EquationArrays equationsIntoArrays(List<EquationFloat> equationFloats) {
    float[] as = new float[equationFloats.size()];
    float[] bs = new float[equationFloats.size()];
    float[] cs = new float[equationFloats.size()];
    for (int i = 0; i < equationFloats.size(); i++) {
      EquationFloat equationFloat = equationFloats.get(i);
      as[i] = equationFloat.a();
      bs[i] = equationFloat.b();
      cs[i] = equationFloat.c();
    }
    return new EquationArrays(as, bs, cs);
  }

  private record EquationArrays(float[] as, float[] bs, float[] cs) {
  }
}
