/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.oxiane.formation.java.optimisation.benches;

import com.oxiane.formation.java.optimisation.floats.*;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Solving2ndDegreeEquationsFloats {

  @State(Scope.Benchmark)
  public static class InputData {
    @Param({"100","1000","10000"})
    private int size;

    final List<EquationFloat> equations;

    public InputData() {
      equations = new ArrayList<>(size);
      Random random = new Random(123456789l);
      IntStream.range(0, size)
               .forEach(
                   i -> equations.add(
                       new EquationFloat(
                           random.nextFloat(-1000, 1000),
                           random.nextFloat(-1000, 1000),
                           random.nextFloat(-1000, 1000)))
               );
    }
    List<EquationFloat> equations() {
      return equations;
    }
  }

  @Benchmark
  public List<EquationSolutionFloat> regular(InputData data) {
    return
        new Solver2NdDegreeEquationFloatRegular()
            .solve(data.equations());
  }

  @Benchmark
  public List<EquationSolutionFloat> vector(InputData data) {
    return
        new Solver2NdDegreeEquationFloatVector()
            .solve(data.equations());
  }

  @Benchmark
  public List<EquationSolutionFloat> vectorCompress(InputData data) {
    return
        new Solver2NdDegreeEquationFloatVectorCompress()
            .solve(data.equations());
  }

}
