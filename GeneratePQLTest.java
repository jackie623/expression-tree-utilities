
import com.physion.ovation.expression_tree.utils.core;
import com.physion.ovation.gui.ebuilder.expression.*;

import java.util.ArrayList;

public class GeneratePQLTest {
    public static void main(String[] args)
    {
        ArrayList<IExpression> operands = new ArrayList<IExpression>();
        operands.add(new Int32LiteralValueExpression(10));
        operands.add(new TimeLiteralValueExpression(new java.util.Date()));

        System.out.println(core.generatePQL(
            new OperatorExpression("AND", operands)
        ));
    }
}
