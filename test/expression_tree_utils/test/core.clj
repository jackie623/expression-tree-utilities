(ns expression-tree-utils.test.core
  (:use [expression-tree-utils.core])
  (:use [lazytest.deftest]))

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

(deftest value-expressions
  (testing "Literal value expressions"
    (testing "boolean literal value expressions"
      (is (= (generate-pql (BooleanLiteralValueExpression. true)) "true"))
      (is (= (generate-pql (BooleanLiteralValueExpression. false)) "false")))

    (testing "integer literal value expressions"
      (is (= (generate-pql (Int32LiteralValueExpression. -1)) (str -1)))
      (is (= (generate-pql (Int32LiteralValueExpression. 0)) (str 0)))
      (is (= (generate-pql (Int32LiteralValueExpression. 1)) (str 1)))
      (is (= (generate-pql (Int32LiteralValueExpression. 2)) (str 2)))
      (map (range -10 10 5)
        #(is (= (generate-pql (Int32LiteralValueExpression. %)) (str %)))))
    )

  (testing "floating point literal value expressions"
    (is (= (generate-pql (Float64LiteralValueExpression. 0)) (str 0.0)))
    (is (= (generate-pql (Float64LiteralValueExpression. Math/PI)) (str Math/PI)))
    (is (= (generate-pql (Float64LiteralValueExpression. (* -1 Math/PI))) (str (* -1 Math/PI))))
    )

  (testing "string literal value expressions"
    (is (= (generate-pql (StringLiteralValueExpression. "")) ""))
    )

  (testing "class literal value expressions"
    (is (= (generate-pql (ClassLiteralValueExpression. "ovation.Epoch")) "class:ovation.Epoch"))
    (is (= (generate-pql (ClassLiteralValueExpression. "Foo")) "class:Foo"))
    (is (= (generate-pql (ClassLiteralValueExpression. "")) "class:"))
    )

  (testing "time literal value expressions"
    (let [calendarDate (GregorianCalendar. 1979 1 23 1 23 45)]
      (is (= (generate-pql (TimeLiteralValueExpression. (.getTime calendarDate)))
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
              )))
      ))
  )
