package visitor;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public enum TypeLexeme {
    SHORT(asList("short", "short int"), 0),
    INT(singletonList("int"), 1),
    LONG(asList("long", "long int"), 2),
    LONG_LONG(asList("long long", "long long int"), 3),
    VOID(singletonList("void"), -1),
    UNKNOWN(emptyList(), -2);

    private List<String> names;
    private int id;

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
}
