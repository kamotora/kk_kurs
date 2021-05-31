package visitor;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public enum TypeLexeme {
    SHORT(asList("short", "shortint"), 0),
    INT(singletonList("int"), 1),
    LONG(asList("long", "longint"), 2),
    LONG_LONG(asList("longlong", "longlongint"), 3),
    VOID(singletonList("void"), -1),
    UNKNOWN(emptyList(), -2);

    private final List<String> names;
    private final int id;

    TypeLexeme(List<String> names, int id) {
        this.names = names;
        this.id = id;
    }

    public static TypeLexeme getType(String typeName) {
        if (StringUtils.isBlank(typeName))
            return UNKNOWN;
        return Arrays.stream(values())
                .filter(type -> type.names.contains(typeName))
                .findFirst()
                .orElse(UNKNOWN);
    }

    public static TypeLexeme max(TypeLexeme first, TypeLexeme second) {
        if (first == UNKNOWN || second == UNKNOWN)
            return UNKNOWN;
        int max = Integer.max(first.id, second.id);
        return max == first.id ? first : second;
    }

    public int getId() {
        return id;
    }
}
