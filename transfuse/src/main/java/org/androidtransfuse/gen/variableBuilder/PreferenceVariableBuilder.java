package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class PreferenceVariableBuilder implements VariableBuilder {

    private final ASTType preferenceType;
    private final String preferenceName;
    private final InjectionNode preferenceManagerInjectionNode;
    private final TypedExpressionFactory typedExpressionFactory;
    private final InjectionExpressionBuilder injectionExpressionBuilder;

    private Map<ASTType, PrefGetBuilder> accessorMethods = new HashMap<ASTType, PrefGetBuilder>();

    @Inject
    public PreferenceVariableBuilder(@Assisted ASTType preferenceType,
                                     @Assisted String preferenceName,
                                     @Assisted InjectionNode preferenceManagerInjectionNode,
                                     TypedExpressionFactory typedExpressionFactory, InjectionExpressionBuilder injectionExpressionBuilder, ASTClassFactory astClassFactory) {
        this.preferenceName = preferenceName;
        this.preferenceManagerInjectionNode = preferenceManagerInjectionNode;
        this.typedExpressionFactory = typedExpressionFactory;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.preferenceType = preferenceType;

        accessorMethods.put(astClassFactory.getType(String.class), new PrefGetBuilder("getString", JExpr.lit("")));
        accessorMethods.put(astClassFactory.getType(long.class), new PrefGetBuilder("getLong", JExpr.lit(0L)));
        accessorMethods.put(astClassFactory.getType(int.class), new PrefGetBuilder("getInt", JExpr.lit(0)));
        accessorMethods.put(astClassFactory.getType(float.class), new PrefGetBuilder("getFloat", JExpr.lit(0F)));
        accessorMethods.put(astClassFactory.getType(boolean.class), new PrefGetBuilder("getBoolean", JExpr.lit(false)));
        accessorMethods.put(astClassFactory.getType(Long.class), new PrefGetBuilder("getLong", JExpr.lit(0L)));
        accessorMethods.put(astClassFactory.getType(Integer.class), new PrefGetBuilder("getInt", JExpr.lit(0)));
        accessorMethods.put(astClassFactory.getType(Float.class), new PrefGetBuilder("getFloat", JExpr.lit(0F)));
        accessorMethods.put(astClassFactory.getType(Boolean.class), new PrefGetBuilder("getBoolean", JExpr.lit(false)));
    }

    @Override
    public TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        TypedExpression preferenceManagerExpression = injectionExpressionBuilder.buildVariable(injectionBuilderContext, preferenceManagerInjectionNode);

        JExpression expression = invokePreferenceMethod(preferenceManagerExpression.getExpression());
        return typedExpressionFactory.build(preferenceType, expression);
    }

    private JExpression invokePreferenceMethod(JExpression preferences) {
        if (accessorMethods.containsKey(preferenceType)) {
            PrefGetBuilder getBuilder = accessorMethods.get(preferenceType);
            return preferences.invoke(getBuilder.getName()).arg(preferenceName).arg(getBuilder.getLit());
        }
        //todo: throw exception?
        return preferences;

    }

    private static final class PrefGetBuilder {
        private String name;
        private JExpression lit;

        private PrefGetBuilder(String name, JExpression lit) {
            this.name = name;
            this.lit = lit;
        }

        public JExpression getLit() {
            return lit;
        }

        public String getName() {
            return name;
        }
    }
}
