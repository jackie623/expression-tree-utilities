
import com.physion.ovation.expression_tree.utils.core;
import com.physion.ovation.gui.ebuilder.ExpressionBuilder;
import com.physion.ovation.gui.ebuilder.expression.*;

import java.util.ArrayList;

public class GeneratePQLTest {
    public static void main(String[] args)
    {

        java.lang.Thread.currentThread().setContextClassLoader(GeneratePQLTest.class.getClassLoader()); 
        
        ExpressionBuilder.ReturnValue rv = core.EditExpressionTree(null);
        ExpressionTree tree = rv.expressionTree;
        System.out.println(core.generatePQL(
            tree.getRootExpression()
        ));
        System.exit(0);
    }
    
    public static String edit()
    {
        java.lang.Thread.currentThread().setContextClassLoader(GeneratePQLTest.class.getClassLoader()); 
        
        ExpressionBuilder.ReturnValue rv = core.EditExpressionTree(null);
        ExpressionTree tree = rv.expressionTree;
        
        return core.generatePQL(tree.getRootExpression());
        
    }
}
