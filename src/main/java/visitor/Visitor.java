package visitor;


import antlr.CPP14BaseVisitor;
import antlr.CPP14Parser;
import lombok.extern.log4j.Log4j2;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashMap;
import java.util.Map;

import static antlr.CPP14Lexer.*;

@Log4j2
public class Visitor extends CPP14BaseVisitor<Node> {

    private final Map<String, Node> vars = new HashMap<>();
    private String type;
    private static final boolean DEBUG = true;
    public static final String TRUE = "1";
    public static final String FALSE = "0";
    private static final Node trueNode = new Node(TypeLexeme.INT, TRUE);
    private static final Node falseNode = new Node(TypeLexeme.INT, FALSE);

    @Override
    public Node visitShiftexpression(CPP14Parser.ShiftexpressionContext ctx) {
        if (ctx.getChildCount() > 1) {
            log.info(super.visit(ctx.getChild(2)).getText());
        }
        return super.visitShiftexpression(ctx);
    }

    @Override
    public Node visitLiteral(CPP14Parser.LiteralContext ctx) {
        return new Node(TypeLexeme.INT, ctx.getChild(0).getText());
    }

    @Override
    public Node visitSimpledeclaration(CPP14Parser.SimpledeclarationContext ctx) {
        type = ctx.getChild(0).getText();
        return super.visitSimpledeclaration(ctx);
    }

    @Override
    public Node visitInitdeclarator(CPP14Parser.InitdeclaratorContext ctx) {
        String value = "0";
        String name = ctx.getChild(0).getText();
        TypeLexeme typeLexeme = TypeLexeme.getType(type);
        if (ctx.getChildCount() > 1) {
            Node node = super.visit(ctx.getChild(1));
            value = node.getText();
            if (node.getType().getId() > typeLexeme.getId() && typeLexeme != TypeLexeme.UNKNOWN)
                log.warn("Строка: {}, позиция: {}. Приведение типа {} к типу {}. Часть данных может быть утеряна",
                        ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), node.getType(), typeLexeme);
        }
        if (typeLexeme == TypeLexeme.UNKNOWN) {
            log.error("Строка: {}, позиция: {}. Неизвестный тип {} переменной {}",
                    ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), type, name);
            System.exit(1);
        }
        Node node = new Node(typeLexeme, value);
        if (vars.get(name) == null) {
            vars.put(name, node);
        } else {
            log.error("Строка: {}, позиция: {}. Переменная {} уже определена!", ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), name);
            System.exit(1);
        }
        return super.visitInitdeclarator(ctx);
    }

    @Override
    public Node visitSelectionstatement(CPP14Parser.SelectionstatementContext ctx) {
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
            log.error("Строка: {}, позиция: {}. Невалидный оператор if", e);
            System.exit(1);
        }
        return new Node(TypeLexeme.VOID, "");
    }


    @Override
    public Node visitIdexpression(CPP14Parser.IdexpressionContext ctx) {
        if (vars.get(ctx.getText()) != null) {
            return vars.get(ctx.getText());
        }
        return super.visitIdexpression(ctx);
    }

    @Override
    public Node visitAdditiveexpression(CPP14Parser.AdditiveexpressionContext ctx) {
        if (ctx.getChildCount() == 1) {
            return super.visit(ctx.getChild(0));
        } else {
            Node a = super.visit(ctx.getChild(0));
            Node b = super.visit(ctx.getChild(2));
            if (((Token)ctx.getChild(1).getPayload()).getType() == Plus) {
                return a.add(b);
            } else {
                return a.sub(b);
            }
        }
    }

    @Override
    public Node visitMultiplicativeexpression(CPP14Parser.MultiplicativeexpressionContext ctx) {
        if (ctx.getChildCount() == 1) {
            return super.visit(ctx.getChild(0));
        } else {
            Node a = super.visit(ctx.getChild(0));
            Node b = super.visit(ctx.getChild(2));
            if (((Token)ctx.getChild(1).getPayload()).getType() == Star) {
                return a.mul(b);
            } else {
                return a.div(b);
            }
        }
    }

    @Override
    public Node visitRelationalexpression(CPP14Parser.RelationalexpressionContext ctx) {
        if (ctx.getChildCount() > 1) {
            Node a = super.visit(ctx.getChild(0));
            Node b = super.visit(ctx.getChild(2));
            switch (((Token)ctx.getChild(1).getPayload()).getType()) {
                case LessEqual:
                    return a.lessEqual(b) ? trueNode : falseNode;
                case GreaterEqual:
                    return a.largerEqual(b) ? trueNode : falseNode;
                case Greater:
                    return a.larger(b) ? trueNode : falseNode;
                case Less:
                    return a.less(b) ? trueNode : falseNode;
            }
        }
        return super.visitRelationalexpression(ctx);
    }

    @Override
    public Node visitEqualityexpression(CPP14Parser.EqualityexpressionContext ctx) {
        if (ctx.getChildCount() > 1) {
            ParseTree a = ctx.getChild(0);
            ParseTree b = ctx.getChild(2);
            switch (((Token)ctx.getChild(1).getPayload()).getType()) {
                case Equal:
                    return super.visit(a).equal(super.visit(b)) ? trueNode : falseNode;
                case NotEqual:
                    return super.visit(a).notEqual(super.visit(b)) ? trueNode : falseNode;
            }
        }
        return super.visitEqualityexpression(ctx);
    }

    @Override
    public Node visitAssignmentexpression(CPP14Parser.AssignmentexpressionContext ctx) {
        if (ctx.getChildCount() > 1) {
            String name = ctx.getChild(0).getText();
            Node node = super.visit(ctx.getChild(2));
            if (vars.get(name) != null) {
                vars.put(name, node);
            } else {
                log.error("Строка: {}, позиция: {}. Переменная {} не определена!",
                        ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), name);
                System.exit(1);
            }
            return node;
        } else {
            return super.visitAssignmentexpression(ctx);
        }
    }
}
