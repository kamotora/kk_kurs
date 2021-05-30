package visitor;


import lombok.extern.log4j.Log4j2;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class Visitor extends CPP14ParserBaseVisitor<Node> {

    private final Map<String, Node> vars = new HashMap<>();
    private String type;
    private static final boolean DEBUG = true;
    public static final String TRUE = "1";
    public static final String FALSE = "0";
    private static final Node trueNode = new Node(TypeLexeme.INT, TRUE);
    private static final Node falseNode = new Node(TypeLexeme.INT, FALSE);

    @Override
    public Node visitShiftExpression(CPP14Parser.ShiftExpressionContext ctx) {
        if (ctx.getChildCount() > 1) {
            log.info(super.visit(ctx.getChild(2)).getText());
        }
        return super.visitShiftExpression(ctx);
    }

    @Override
    public Node visitLiteral(CPP14Parser.LiteralContext ctx) {
        return new Node(TypeLexeme.INT, ctx.getChild(0).getText());
    }

    @Override
    public Node visitSimpleDeclaration(CPP14Parser.SimpleDeclarationContext ctx) {
        type = ctx.getChild(0).getText();
        return super.visitSimpleDeclaration(ctx);
    }

    @Override
    public Node visitInitDeclarator(CPP14Parser.InitDeclaratorContext ctx) {
        String value = "0";
        String name = ctx.getChild(0).getText();
        if (ctx.getChildCount() > 1) {
            Node node = super.visit(ctx.getChild(1));
            value = node.getText();
        }
        TypeLexeme typeLexeme = TypeLexeme.getType(type);
        if (typeLexeme == TypeLexeme.UNKNOWN) {
            log.error("Неизвестный тип {} переменной {}", type, name);
        }
        Node node = new Node(typeLexeme, value);
        if (vars.get(name) == null) {
            vars.put(name, node);
        } else {
            log.error("Переменная {} уже определена!", name);
        }
        return super.visitInitDeclarator(ctx);
    }

    @Override
    public Node visitSelectionStatement(CPP14Parser.SelectionStatementContext ctx) {
        int count = ctx.getChildCount();
        try {
            if (count == 5) {
                // if
                Node condition = super.visit(ctx.getChild(2)); // посещаем
                if (condition.getText().equals(TRUE)) {
                    super.visit(ctx.getChild(4));
                }
            } else if (count == 7) {
                //if else
                Node condition = super.visit(ctx.getChild(2)); // посещаем
                if (condition.getText().equals(TRUE)) {
                    // true
                    super.visit(ctx.getChild(4));
                } else {
                    //false
                    super.visit(ctx.getChild(6));
                }
            }
        } catch (Exception e) {
            log.error("Невалидный оператор if", e);
            System.exit(1);
        }
        return new Node(TypeLexeme.VOID, "");
    }


    @Override
    public Node visitIdExpression(CPP14Parser.IdExpressionContext ctx) {
        if (vars.get(ctx.getText()) != null) {
            return vars.get(ctx.getText());
        }
        return super.visitIdExpression(ctx);
    }

    @Override
    public Node visitAdditiveExpression(CPP14Parser.AdditiveExpressionContext ctx) {
        if (ctx.getChildCount() == 1) {
            return super.visit(ctx.getChild(0));
        } else {
            Node a = super.visit(ctx.getChild(0));
            Node b = super.visit(ctx.getChild(2));
            if (ctx.getChild(1).getText().equals("+")) {
                return a.add(b);
            } else {
                return a.sub(b);
            }
        }
    }

    @Override
    public Node visitMultiplicativeExpression(CPP14Parser.MultiplicativeExpressionContext ctx) {
        if (ctx.getChildCount() == 1) {
            return super.visit(ctx.getChild(0));
        } else {
            Node a = super.visit(ctx.getChild(0));
            Node b = super.visit(ctx.getChild(2));
            if (ctx.getChild(1).getText().equals("*")) {
                return a.mul(b);
            } else {
                return a.div(b);
            }
        }
    }

    @Override
    public Node visitRelationalExpression(CPP14Parser.RelationalExpressionContext ctx) {
        if (ctx.getChildCount() > 1) {
            Node a = super.visit(ctx.getChild(0));
            Node b = super.visit(ctx.getChild(2));
            switch (ctx.getChild(1).getText()) {
                case "<=":
                    return a.lessEqual(b) ? trueNode : falseNode;
                case ">=":
                    return a.largerEqual(b) ? trueNode : falseNode;
                case ">":
                    return a.larger(b) ? trueNode : falseNode;
                case "<":
                    return a.less(b) ? trueNode : falseNode;
            }
        }
        return super.visitRelationalExpression(ctx);
    }

    @Override
    public Node visitEqualityExpression(CPP14Parser.EqualityExpressionContext ctx) {
        if (ctx.getChildCount() > 1) {
            ParseTree a = ctx.getChild(0);
            ParseTree b = ctx.getChild(2);
            switch (ctx.getChild(1).getText()) {
                case "==":
                    return super.visit(a).equal(super.visit(b)) ? trueNode : falseNode;
                case "!=":
                    return super.visit(a).notEqual(super.visit(b)) ? trueNode : falseNode;
            }
        }
        return super.visitEqualityExpression(ctx);
    }

    @Override
    public Node visitAssignmentExpression(CPP14Parser.AssignmentExpressionContext ctx) {
        if (ctx.getChildCount() > 1) {
            String name = ctx.getChild(0).getText();
            Node node = super.visit(ctx.getChild(2));
            if (vars.get(name) != null) {
                vars.put(name, node);
            } else {
                log.error("Переменная {} не определена!", name);
            }
            return node;
        } else {
            return super.visitAssignmentExpression(ctx);
        }
    }
}
