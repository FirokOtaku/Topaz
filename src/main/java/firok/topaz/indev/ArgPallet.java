package firok.topaz.indev;

import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static firok.topaz.general.Collections.*;

/**
 * 用于解析命令行参数的工具类
 * @author Firok
 * */
public class ArgPallet
{
    public interface GroupArg<TypeArg extends Serializable>
    {
        String getGroupName();
        Class<TypeArg> getArgType();
        TypeArg parseValue(String raw);

        @SuppressWarnings("unchecked")
        default TypeArg[] parseValues(String... raws)
        {
            var size = sizeOf(raws);
            var ret = (TypeArg[]) Array.newInstance(getArgType(), size);
            if(size != 0)
            {
                var step = 0;
                for(String raw : raws)
                    ret[step++] = parseValue(raw);
            }
            return ret;
        }

        List<TypeArg> getDefaultValue();
    }
    @Getter
    public abstract static class NamedGroupArg<TypeArg extends Serializable> implements GroupArg<TypeArg>
    {
        protected final String groupName;
        protected final Class<TypeArg> argType;
        protected final List<TypeArg> defaultValues;
        protected NamedGroupArg(String groupName, Class<TypeArg> argType, TypeArg[] defaultValues)
        {
            this.groupName = groupName;
            this.argType = argType;
            this.defaultValues = new ArrayList<>();
            if(isNotEmpty(defaultValues))
            {
                this.defaultValues.addAll(Arrays.asList(defaultValues));
            }
        }

        @Override
        public List<TypeArg> getDefaultValue()
        {
            return new ArrayList<>(defaultValues);
        }
    }


    public ArgPallet(GroupArg<?>... groups)
    {
        ;
    }

    public Map<String, List<Serializable>> parse(String... args)
    {
        return args == null ? parse(new ArrayList<>()) : parse(List.of(args));
    }
    public Map<String, List<Serializable>> parse(List<String> args)
    {
        return null;
    }



    public static class strings extends NamedGroupArg<String>
    {
        public strings(String groupName)
        {
            this(groupName, new String[0]);
        }
        public strings(String groupName, String... defaultValues)
        {
            super(groupName, String.class, defaultValues);
        }

        @Override
        public String parseValue(String raw)
        {
            return String.valueOf(raw);
        }
    }
//    public static class ints extends NamedGroupArg<Integer>
//    {
//        public ints(String groupName)
//        {
//            this(groupName, new int[0]);
//        }
//        public ints(String groupName, int... defaultValues)
//        {
//            super(groupName, Integer.class, Maths.box);
//        }
//
//        @Override
//        public Integer parseValue(String raw)
//        {
//            return Integer.parseInt(raw);
//        }
//    }
}
