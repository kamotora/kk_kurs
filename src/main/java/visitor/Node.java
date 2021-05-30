package visitor;

import java.math.BigInteger;

public class Node {

    private final TypeLexeme typeLexeme;
    private final String text;

    public Node(TypeLexeme type, String text) {
        this.typeLexeme = type;
        this.text = text;
    }

    public Node add(Node node) {
        BigInteger a = new BigInteger(text);
        BigInteger b = new BigInteger(node.getText());
        TypeLexeme typeLexeme = getType(node.typeLexeme);
        return new Node(typeLexeme, a.add(b).toString());
    }

    public Node sub(Node node) {
        BigInteger a = new BigInteger(text);
        BigInteger b = new BigInteger(node.getText());
        TypeLexeme typeLexeme = getType(node.typeLexeme);
        return new Node(typeLexeme, a.subtract(b).toString());
    }

    public Node mul(Node node) {
        BigInteger a = new BigInteger(text);
        BigInteger b = new BigInteger(node.getText());
        TypeLexeme typeLexeme = getType(node.typeLexeme);
        return new Node(typeLexeme, a.multiply(b).toString());
    }

    public Node div(Node node) {
        BigInteger a = new BigInteger(text);
        BigInteger b = new BigInteger(node.getText());
        TypeLexeme typeLexeme = getType(node.typeLexeme);
        return new Node(typeLexeme, a.divide(b).toString());
    }

    public boolean lessEqual(Node node) {
        BigInteger a = new BigInteger(text);
        BigInteger b = new BigInteger(node.getText());
        return a.compareTo(b) <= 0;
    }

    public boolean largerEqual(Node node) {
        BigInteger a = new BigInteger(text);
        BigInteger b = new BigInteger(node.getText());
        return a.compareTo(b) >= 0;
    }

    public boolean larger(Node node) {
        BigInteger a = new BigInteger(text);
        BigInteger b = new BigInteger(node.getText());
        return a.compareTo(b) > 0;
    }

    public boolean less(Node node) {
        BigInteger a = new BigInteger(text);
        BigInteger b = new BigInteger(node.getText());
        return a.compareTo(b) < 0;
    }

    public boolean equal(Node node) {
        BigInteger a = new BigInteger(text);
        BigInteger b = new BigInteger(node.getText());
        return a.compareTo(b) == 0;
    }

    public boolean notEqual(Node node) {
        BigInteger a = new BigInteger(text);
        BigInteger b = new BigInteger(node.getText());
        return a.compareTo(b) != 0;
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