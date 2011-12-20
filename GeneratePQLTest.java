
import com.physion.ovation.expression_tree.utils.core;
import com.physion.ovation.gui.ebuilder.ExpressionBuilder;
import com.physion.ovation.gui.ebuilder.expression.*;

import java.util.ArrayList;

public class GeneratePQLTest {
    public static void main(String[] args)
    {

        ExpressionBuilder.ReturnValue rv = core.EditExpressionTree(null);
        ExpressionTree tree = rv.expressionTree;
        System.out.println(core.generatePQL(
            tree.getRootExpression()
        ));
    }
}
