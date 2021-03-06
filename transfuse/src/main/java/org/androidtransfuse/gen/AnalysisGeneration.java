package org.androidtransfuse.gen;

import org.androidtransfuse.analysis.Analysis;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class AnalysisGeneration<T> implements Generator<ASTType> {

    private final Analysis<T> analysis;
    private final Generator<T> generator;

    public AnalysisGeneration(Analysis<T> analysis,
                              Generator<T> generator) {
        this.analysis = analysis;
        this.generator = generator;
    }

    @Override
    public void generate(ASTType astType) {
        T descriptor = analysis.analyze(astType);

        generator.generate(descriptor);
    }
}
