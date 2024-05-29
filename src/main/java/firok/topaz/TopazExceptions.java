package firok.topaz;

import firok.topaz.general.*;

import java.util.Locale;

/**
 * 所有 Topaz 库会抛出的异常类型
 * @since 7.0.0
 * @author Firok
 * */
public enum TopazExceptions implements CodeExceptionThrower
{
    StructureDuplicate(5201),

    FileSystemCreationFailed(5251),

    EmitterAlreadyClosed(5301),

    JvmNotFound(5351),

    RegistryKeyDuplicate(5401),
    RegistryAlreadyLocked(5402),

    LogicsExpressionEmpty(5451),

    ParamValueOutOfRange(5501),
    ParamValueNoneNull(5502),
    ParamFormatError(5503),
    ParamValueLogicError(5504),

    DatabaseOperationError(5551),
    BeanInstantiationError(5552),

    ColorValueOutOfRange(5601),

    I18NDefaultResourceNotFound(5702),

    NullFunction(5850),
    NoMainMethod(5851),
    NoClassResource(5852),
    IOError(5853),
    ;

    private static final I18N i18n = new I18N("/firok/topaz/errors", Locale.CHINA, Topaz.class);

    public final int code;
    TopazExceptions(int code) { this.code = code; }

    @Override
    public int getExceptionCode() { return code; }

    @Override
    public I18N getI18N() { return i18n; }
}
