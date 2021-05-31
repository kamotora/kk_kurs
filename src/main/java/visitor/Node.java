package visitor;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class Node {

    private final TypeLexeme typeLexeme;
    private final String text;

    public Node(TypeLexeme type, String text) {
        this.typeLexeme = type;
        this.text = text;
    }

    public Node add(Node node) {
        BigInteger a = getNumber(text);
        BigInteger b = getNumber(node.getText());
        TypeLexeme typeLexeme = getType(node.typeLexeme);
        return new Node(typeLexeme, a.add(b).toString());
    }

    public Node sub(Node node) {
        BigInteger a = getNumber(text);
        BigInteger b = getNumber(node.getText());
        TypeLexeme typeLexeme = getType(node.typeLexeme);
        return new Node(typeLexeme, a.subtract(b).toString());
    }

    public Node mul(Node node) {
        BigInteger a = getNumber(text);
        BigInteger b = getNumber(node.getText());
        TypeLexeme typeLexeme = getType(node.typeLexeme);
        return new Node(typeLexeme, a.multiply(b).toString());
    }

    public Node div(Node node) {
        BigInteger a = getNumber(text);
        BigInteger b = getNumber(node.getText());
        TypeLexeme typeLexeme = getType(node.typeLexeme);
        return new Node(typeLexeme, a.divide(b).toString());
    }

    public boolean lessEqual(Node node) {
        BigInteger a = getNumber(text);
        BigInteger b = getNumber(node.getText());
        return a.compareTo(b) <= 0;
    }

    public boolean largerEqual(Node node) {
        BigInteger a = getNumber(text);
        BigInteger b = getNumber(node.getText());
        return a.compareTo(b) >= 0;
    }

    public boolean larger(Node node) {
        BigInteger a = getNumber(text);
        BigInteger b = getNumber(node.getText());
        return a.compareTo(b) > 0;
    }

    public boolean less(Node node) {
        BigInteger a = getNumber(text);
        BigInteger b = getNumber(node.getText());
        return a.compareTo(b) < 0;
    }

    public boolean equal(Node node) {
        BigInteger a = getNumber(text);
        BigInteger b = getNumber(node.getText());
        return a.compareTo(b) == 0;
    }

    public boolean notEqual(Node node) {
        BigInteger a = getNumber(text);
        BigInteger b = getNumber(node.getText());
        return a.compareTo(b) != 0;
    }

    @NotNull
    private BigInteger getNumber(String text) {
        if (StringUtils.containsAny(text, 'x', 'X'))
            return new BigInteger(text.substring(2), 16);
        return new BigInteger(text);
    }

    public TypeLexeme getType() {
        return typeLexeme;
    }

    private TypeLexeme getType(TypeLexeme type) {
        return TypeLexeme.max(this.typeLexeme, type);
    }

    @Override
    public String toString() {
        return "Node " + typeLexeme + " " + text;
    }

    public String getText() {
        return text;
    }
}