package utils.jpa;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class ParamType {
    private final static Map<Class<?>, Integer> paramTypeMap = new HashMap<Class<?>, Integer>();
    static {
        paramTypeMap.put(Integer.class, ParamObject.TYPE_INTEGER);
        paramTypeMap.put(Long.class, ParamObject.TYPE_LONG);
        paramTypeMap.put(BigDecimal.class, ParamObject.TYPE_BIG_DECIMAL);
        paramTypeMap.put(String.class, ParamObject.TYPE_STRING);
        paramTypeMap.put(Date.class, ParamObject.TYPE_DATE);
        paramTypeMap.put(Timestamp.class, ParamObject.TYPE_TIMES_STAMP);
        paramTypeMap.put(Blob.class, ParamObject.TYPE_BLOB);
        paramTypeMap.put(Clob.class, ParamObject.TYPE_CLOB);
    }

    public final static int get(Object o) {
        if (o == null)
            return ParamObject.TYPE_UNKNOWN;
        Integer type = paramTypeMap.get(o.getClass());
        if (type == null)
            return ParamObject.TYPE_UNKNOWN;
        else
            return type.intValue();
    }
}
