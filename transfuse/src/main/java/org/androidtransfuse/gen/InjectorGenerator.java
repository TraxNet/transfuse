package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.AnalysisContextFactory;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.InjectionNodeFactory;
import org.androidtransfuse.gen.componentBuilder.MirroredMethodGenerator;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectorGenerator implements Generator<ASTType> {

    private static final String IMPL_EXT = "Impl";

    private final JCodeModel codeModel;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final AnalysisContextFactory analysisContextFactory;
    private final InjectionNodeBuilderRepository injectionNodeBuilderRepository;
    private final InjectorsGenerator injectorsGenerator;
    private final ClassGenerationUtil generationUtil;

    @Inject
    public InjectorGenerator(JCodeModel codeModel,
                             InjectionFragmentGenerator injectionFragmentGenerator,
                             ComponentBuilderFactory componentBuilderFactory,
                             AnalysisContextFactory analysisContextFactory,
                             InjectionNodeBuilderRepository injectionNodeBuilderRepository,
                             InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                             InjectorsGenerator injectorsGenerator,
                             ClassGenerationUtil generationUtil) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.componentBuilderFactory = componentBuilderFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.injectionNodeBuilderRepository = injectionNodeBuilderRepository;
        this.injectorsGenerator = injectorsGenerator;
        this.generationUtil = generationUtil;
        injectionNodeBuilderRepositoryFactory.addModuleConfiguration(this.injectionNodeBuilderRepository);
    }

    @Override
    public void generate(ASTType descriptor) {

        if (descriptor.isConcreteClass()) {
            throw new TransfuseAnalysisException("Unable to build injector from concrete class");
        }

        try {
            JDefinedClass implClass = generationUtil.defineClass(descriptor.getPackageClass().append(IMPL_EXT));
            JClass interfaceClass = codeModel.ref(descriptor.getName());

            implClass._implements(interfaceClass);

            for (ASTMethod interfaceMethod : descriptor.getMethods()) {
                MirroredMethodGenerator mirroredMethodGenerator = componentBuilderFactory.buildMirroredMethodGenerator(interfaceMethod, false);
                MethodDescriptor methodDescriptor = mirroredMethodGenerator.buildMethod(implClass);
                JBlock block = methodDescriptor.getMethod().body();

                AnalysisContext context = analysisContextFactory.buildAnalysisContext(injectionNodeBuilderRepository);
                InjectionNodeFactory injectionNodeFactory = componentBuilderFactory.buildInjectionNodeFactory(interfaceMethod.getReturnType(), context);

                //Injections
                InjectionNode returnType = injectionNodeFactory.buildInjectionNode(methodDescriptor);
                Map<InjectionNode, TypedExpression> expressionMap =
                        injectionFragmentGenerator.buildFragment(
                                block,
                                implClass,
                                returnType);

                block._return(expressionMap.get(returnType).getExpression());

            }

            injectorsGenerator.generateInjectors(interfaceClass, implClass);

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class already exists for generated type " + descriptor.getName(), e);
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Target class not found", e);
        }
    }
}
