package org.androidtransfuse.analysis.adapter;

import org.androidtransfuse.util.ElementVisitorAdaptor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

/**
 * Builds the appropriate (expected) ASTType for the visited Element
 *
 * @param <T>
 */
public class ASTTypeElementConverter<T> extends ElementVisitorAdaptor<T, Void> {

    private static final String CONSTRUCTOR_IDENTIFIER = "<init>";

    private final Class<T> astTypeClass;
    private final ASTElementFactory astElementFactory;

    public ASTTypeElementConverter(Class<T> astTypeClass,
                                   ASTElementFactory astElementFactory) {
        this.astTypeClass = astTypeClass;
        this.astElementFactory = astElementFactory;
    }

    @Override
    public T visitType(TypeElement typeElement, Void aVoid) {
        if (astTypeClass.isAssignableFrom(ASTType.class)) {
            return (T) astElementFactory.getType(typeElement);
        }
        return null;
    }

    @Override
    public T visitVariable(VariableElement variableElement, Void aVoid) {
        if (astTypeClass.isAssignableFrom(ASTField.class)) {
            return (T) astElementFactory.getField(variableElement);
        }
        return null;
    }

    @Override
    public T visitExecutable(ExecutableElement executableElement, Void aVoid) {
        //constructors and methods share this Element, the indication that the method is a constructor
        //is that it is named <init>
        if (executableElement.getSimpleName().contentEquals(CONSTRUCTOR_IDENTIFIER)) {
            if (astTypeClass.isAssignableFrom(ASTConstructor.class)) {
                return (T) astElementFactory.getConstructor(executableElement);
            }
        } else if (astTypeClass.isAssignableFrom(ASTMethod.class)) {
            return (T) astElementFactory.getMethod(executableElement);
        }
        return null;
    }

    @Override
    public T visitTypeParameter(TypeParameterElement typeParameterElement, Void aVoid) {
        if (astTypeClass.isAssignableFrom(ASTParameter.class)) {
            return (T) astElementFactory.getParameter(typeParameterElement);
        }
        return null;
    }
}