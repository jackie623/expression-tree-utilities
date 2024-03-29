(ns com.physion.ovation.expression.utils.test.core
  (:use [com.physion.ovation.expression.utils.core])
  (:use [lazytest.describe])
  (:use [lazytest.expect])
  (:require [lazytest.color]))

(import (java.util
          GregorianCalendar
          Date
          Calendar
          ArrayList))

(import (com.physion.ebuilder.expression
          BooleanLiteralValueExpression
          Int32LiteralValueExpression
          Float64LiteralValueExpression
          StringLiteralValueExpression
          ClassLiteralValueExpression
          TimeLiteralValueExpression
          AttributeExpression
          OperatorExpression))

; For IntelliJ... skip colorization
(lazytest.color/set-colorize false)

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
    (= (generate-pql (StringLiteralValueExpression. "foo")) "\"foo\"")
    )

  (it "Generates class literal value expressions"
    (= (generate-pql (ClassLiteralValueExpression. "ovation.Epoch")) "class:ovation.Epoch")
    (= (generate-pql (ClassLiteralValueExpression. "Foo")) "class:Foo")
    (= (generate-pql (ClassLiteralValueExpression. "")) "class:")
    )

  (it "Generates time literal value expressions"
    (= (generate-pql (TimeLiteralValueExpression. (.getTime (GregorianCalendar. 1979 1 23 1 23 45))))
      (str (+ 1 (.get (GregorianCalendar. 1979 1 23 1 23 45) Calendar/MONTH))
        "/"
        (.get (GregorianCalendar. 1979 1 23 1 23 45) Calendar/DAY_OF_MONTH)
        "/"
        (.get (GregorianCalendar. 1979 1 23 1 23 45) Calendar/YEAR)
        " "
        (.get (GregorianCalendar. 1979 1 23 1 23 45) Calendar/HOUR_OF_DAY)
        ":"
        (.get (GregorianCalendar. 1979 1 23 1 23 45) Calendar/MINUTE)
        ":"
        (.get (GregorianCalendar. 1979 1 23 1 23 45) Calendar/SECOND)
        ":"
        (.get (GregorianCalendar. 1979 1 23 1 23 45) Calendar/MILLISECOND)
        " "
        (if (= (.get (GregorianCalendar. 1979 1 23 1 23 45) Calendar/AM_PM) Calendar/AM)
          "AM"
          "PM")
        ))

    (= (generate-pql (TimeLiteralValueExpression. (.getTime (GregorianCalendar. 1979 1 23 14 23 45))))
      (str (+ 1 (.get (GregorianCalendar. 1979 1 23 14 23 45) Calendar/MONTH))
        "/"
        (.get (GregorianCalendar. 1979 1 23 14 23 45) Calendar/DAY_OF_MONTH)
        "/"
        (.get (GregorianCalendar. 1979 1 23 14 23 45) Calendar/YEAR)
        " "
        (.get (GregorianCalendar. 1979 1 23 14 23 45) Calendar/HOUR_OF_DAY)
        ":"
        (.get (GregorianCalendar. 1979 1 23 14 23 45) Calendar/MINUTE)
        ":"
        (.get (GregorianCalendar. 1979 1 23 14 23 45) Calendar/SECOND)
        ":"
        (.get (GregorianCalendar. 1979 1 23 14 23 45) Calendar/MILLISECOND)
        " "
        (if (= (.get (GregorianCalendar. 1979 1 23 14 23 45) Calendar/AM_PM) Calendar/AM)
          "AM"
          "PM")
        ))
    )

  (it "Generates Attribute expressions"
    (let [attrName "some-attribute"]
      (= (generate-pql (AttributeExpression. attrName)) attrName)
      ))

  (testing "Generates operator expressions"
    (testing "with 0 operands"
      (it "Generates pql with 0 operands"
        (= (generate-pql (OperatorExpression. "my_operator" (ArrayList.))) (str (.toUpperCase "my_operator") "()"))
        ))
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

    (testing "converting comparison operators to functional form"
      (it "converts *"
        (= (generate-pql (OperatorExpression. "*" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "MULTIPLY(10,10)"))

      (it "converts /"
        (= (generate-pql (OperatorExpression. "/" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "DIVIDE(10,10)"))

      (it "converts %"
        (= (generate-pql (OperatorExpression. "%" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "MODULO(10,10)"))

      (it "coverts binary +"
        (= (generate-pql (OperatorExpression. "+" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "PLUS(10,10)"))

      (it "does not convert unary +"
        (= (generate-pql (OperatorExpression. "+" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10))))) "+10"))

      (it "coverts binary -"
        (= (generate-pql (OperatorExpression. "-" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "MINUS(10,10)"))

      (it "does not convert unary -"
        (= (generate-pql (OperatorExpression. "-" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10))))) "-10"))

      (it "converts <"
        (= (generate-pql (OperatorExpression. "<" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "LT(10,10)"))

      (it "converts <="
        (= (generate-pql (OperatorExpression. "<=" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "LE(10,10)"))

      (it "converts >"
        (= (generate-pql (OperatorExpression. ">" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "GT(10,10)"))

      (it "converts >="
        (= (generate-pql (OperatorExpression. ">=" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "GE(10,10)"))



      (it "converts =="
        (= (generate-pql (OperatorExpression. "==" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "EQ(10,10)"))

      (it "converts ="
        (= (generate-pql (OperatorExpression. "=" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "EQ(10,10)"))

      (it "converts !="
        (= (generate-pql (OperatorExpression. "!=" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "NE(10,10)"))

      (it "converts <>"
        (= (generate-pql (OperatorExpression. "<>" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "NE(10,10)"))

      )
    (testing "hoisting singleton conjunctions"
      (it "does not hoist AND(foo,bar) => AND(foo,barr)"
        (= (generate-pql (OperatorExpression. "AND" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "AND(10,10)"))

      (it "hoists AND(ex) => ex"
        (= (generate-pql (OperatorExpression. "AND" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10))))) "10"))

      (it "does not hoist OR(foo,bar) => OR(foo,bar)"
        (= (generate-pql (OperatorExpression. "AND" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10)) (.add (Int32LiteralValueExpression. 10))))) "AND(10,10)"))
      (it "hoists OR(ex) => or"
        (= (generate-pql (OperatorExpression. "AND" (doto (new ArrayList) (.add (Int32LiteralValueExpression. 10))))) "10"))
      )

    (it "infixes the attribute access (.) operator"
      (= (generate-pql (OperatorExpression. "." (doto (new ArrayList) (.add (AttributeExpression. "foo")) (.add (AttributeExpression. "bar"))))) "foo.bar"))
    )

  )
