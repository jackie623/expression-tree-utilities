(ns expression-tree-utils.test.core
  (:use [expression-tree-utils.core])
  (:use [lazytest.describe]))

(import (java.util
          GregorianCalendar
          Date
          Calendar))
(import (com.physion.ovation.gui.ebuilder.expression
          BooleanLiteralValueExpression
          Int32LiteralValueExpression
          Float64LiteralValueExpression
          StringLiteralValueExpression
          ClassLiteralValueExpression
          TimeLiteralValueExpression))


(describe expression-pql
  (it "enerates boolean literal value expressions"
      (= (generate-pql (BooleanLiteralValueExpression. true)) "true")
      (= (generate-pql (BooleanLiteralValueExpression. false)) "false"))

    (it "generates integer literal value expressions"
      (= (generate-pql (Int32LiteralValueExpression. -1)) (str -1))
      (= (generate-pql (Int32LiteralValueExpression. 0)) (str 0))
      (= (generate-pql (Int32LiteralValueExpression. 1)) (str 1))
      (= (generate-pql (Int32LiteralValueExpression. 2)) (str 2))
      (map (range -10 10 5)
        #(= (generate-pql (Int32LiteralValueExpression. %)) (str %))))


  (it "generates floating point literal value expressions"
    (= (generate-pql (Float64LiteralValueExpression. 0)) (str 0.0))
    (= (generate-pql (Float64LiteralValueExpression. Math/PI)) (str Math/PI))
    (= (generate-pql (Float64LiteralValueExpression. (* -1 Math/PI))) (str (* -1 Math/PI)))
    )

  (it "generates string literal value expressions"
    (= (generate-pql (StringLiteralValueExpression. "")) "")
    )

  (it "generates class literal value expressions"
    (= (generate-pql (ClassLiteralValueExpression. "ovation.Epoch")) "class:ovation.Epoch")
    (= (generate-pql (ClassLiteralValueExpression. "Foo")) "class:Foo")
    (= (generate-pql (ClassLiteralValueExpression. "")) "class:")
    )

  (it "generates time literal value expressions"
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
  )
