(ns com.physion.ovation.expression_tree.utils.test.core
  (:use [com.physion.ovation.expression_tree.utils.core])
  (:use [lazytest.describe]))

(import (java.util
          GregorianCalendar
          Date
          Calendar
          ArrayList))

(import (com.physion.ovation.gui.ebuilder.expression
          BooleanLiteralValueExpression
          Int32LiteralValueExpression
          Float64LiteralValueExpression
          StringLiteralValueExpression
          ClassLiteralValueExpression
          TimeLiteralValueExpression
          AttributeExpression
          OperatorExpression))


(describe expression-pql
  (it "Generates boolean literal value expressions"
    (= (generate-pql (BooleanLiteralValueExpression. true)) "true")
    (= (generate-pql (BooleanLiteralValueExpression. false)) "false"))

  (it "Generates integer literal value expressions"
    (= (generate-pql (Int32LiteralValueExpression. -1)) (str -1))
    (= (generate-pql (Int32LiteralValueExpression. 0)) (str 0))
    (= (generate-pql (Int32LiteralValueExpression. 1)) (str 1))
    (= (generate-pql (Int32LiteralValueExpression. 2)) (str 2))
    (map (range -10 10 5)
      #(= (generate-pql (Int32LiteralValueExpression. %)) (str %))))


  (it "Generates floating point literal value expressions"
    (= (generate-pql (Float64LiteralValueExpression. 0)) (str 0.0))
    (= (generate-pql (Float64LiteralValueExpression. Math/PI)) (str Math/PI))
    (= (generate-pql (Float64LiteralValueExpression. (* -1 Math/PI))) (str (* -1 Math/PI)))
    )

  (it "Generates string literal value expressions"
    (= (generate-pql (StringLiteralValueExpression. "")) "")
    )

  (it "Generates class literal value expressions"
    (= (generate-pql (ClassLiteralValueExpression. "ovation.Epoch")) "class:ovation.Epoch")
    (= (generate-pql (ClassLiteralValueExpression. "Foo")) "class:Foo")
    (= (generate-pql (ClassLiteralValueExpression. "")) "class:")
    )

  (it "Generates time literal value expressions"
    (let [calendarDate (GregorianCalendar. 1979 1 23 1 23 45)]
      (= (generate-pql (TimeLiteralValueExpression. (.getTime calendarDate)))
        (str (+ 1 (.get calendarDate Calendar/MONTH))
          "/"
          (.get calendarDate Calendar/DAY_OF_MONTH)
          "/"
          (.get calendarDate Calendar/YEAR)
          " "
          (.get calendarDate Calendar/HOUR_OF_DAY)
          ":"
          (.get calendarDate Calendar/MINUTE)
          ":"
          (.get calendarDate Calendar/SECOND)
          ":"
          (.get calendarDate Calendar/MILLISECOND)
          " "
          (if (= (.get calendarDate Calendar/AM_PM) Calendar/AM)
            "AM"
            "PM")
          ))
      ))

  (it "Generates Attribute expressions"
    (let [attrName "some-attribute"]
      (= (generate-pql (AttributeExpression. attrName)) attrName)
      ))

  (testing "Generates operator expressions"
    (testing "with 0 operands"
      (it "Generates pql with 0 operands"
        (let [operatorName "my_operator"]
          (= (generate-pql (OperatorExpression. operatorName (ArrayList.))) (str (.toUpperCase operatorName) "()"))
          )))
    (testing "with 1 operand"
      (it "Generates pql with 1 operand"
        (let [operatorName "MY_OPERATOR"]
          (let [op1 (Float64LiteralValueExpression. Math/PI)
                opList (ArrayList.)]
            (.add opList op1)
            (= (generate-pql (OperatorExpression. operatorName opList))
              (str operatorName "(" (generate-pql op1) ")"))
            )
          )))
    (testing "with 2 opearnds"
      (it "Generates pql with 2 operands"
        (let [operatorName "MY_OPERATOR"]
          (let [op1 (Float64LiteralValueExpression. Math/PI)
                op2 (StringLiteralValueExpression. "some-string")
                opList (ArrayList.)]
            (.add opList op1)
            (.add opList op2)
            (= (generate-pql (OperatorExpression. operatorName opList))
              (str operatorName "("
                (generate-pql op1)
                ","
                (generate-pql op2)
                ")"))
            )
          )))
    (testing "with 3 opearnds"
      (it "Generates pql with 3 operands"
        (let [operatorName "MY_OPERATOR"]
          (let [op1 (Float64LiteralValueExpression. Math/PI)
                op2 (StringLiteralValueExpression. "some-string")
                op3 (Int32LiteralValueExpression. 10)
                opList (new ArrayList)]
            (.add opList op1)
            (.add opList op2)
            (.add opList op3)
            (= (generate-pql (OperatorExpression. operatorName opList))
              (str operatorName "("
                (generate-pql op1)
                ","
                (generate-pql op2)
                ","
                (generate-pql op3)
                ")"))
            )
          ))
      )
    )

  )
