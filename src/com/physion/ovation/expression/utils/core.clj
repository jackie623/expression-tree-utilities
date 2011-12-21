(ns com.physion.ovation.expression.utils.core
  (:gen-class :main false
              :methods [#^{:static true} [generatePQL [com.physion.ebuilder.expression.IExpression] String]]
              ))

(import (com.physion.ebuilder.expression
          IExpression
          IBooleanLiteralValueExpression
          IInt32LiteralValueExpression
          IFloat64LiteralValueExpression
          IStringLiteralValueExpression
          IClassLiteralValueExpression
          ITimeLiteralValueExpression
          ILiteralValueExpression
          IAttributeExpression
          IOperatorExpression))

(import (java.util
          Calendar
          GregorianCalendar))

;;; Multimethod dispatch for literal value expressions, dispatching on class
(defmulti value-expression-pql class)

;; BooleanLiteralValueExpression
(defmethod value-expression-pql IBooleanLiteralValueExpression [expression]
  (if (= (.booleanValue (.getValue expression)) true)
    "true"
    "false"))

;; Int32LiteralValueExpression
(defmethod value-expression-pql IInt32LiteralValueExpression [expression]
  (str (.getValue expression)))

;; Float64LiteralValueExpression
(defmethod value-expression-pql IFloat64LiteralValueExpression [expression]
  (str (.getValue expression)))

;; StringLiteralValueExpression
(defmethod value-expression-pql IStringLiteralValueExpression [expression]
  (str "\"" (.getValue expression) "\""))

;; ClassLiteralValueExpression
(defmethod value-expression-pql IClassLiteralValueExpression [expression]
  (str "class:" (.getValue expression)))

;; TimeLiteralValueExpression
(defmethod value-expression-pql ITimeLiteralValueExpression [expression]
  (let [localDate (.getTimeValue expression)]
    (str (.getMonthOfYear localDate)
      "/"
      (.getDayOfMonth localDate)
      "/"
      (.getYear localDate)
      " "
      (.getHourOfDay localDate)
      ":"
      (.getMinuteOfHour localDate)
      ":"
      (.getSecondOfMinute localDate)
      ":"
      (.getMillisOfSecond localDate)
      " "
      (if (< (.getHourOfDay localDate) 12)
        "AM"
        "PM")
      )))



;;; Multimethod dispatch for IExpression
(defmulti expression-pql class)

;; ILiteralValueExpression
(defmethod expression-pql ILiteralValueExpression [expression]
  (value-expression-pql expression))

;; IAttributeExpression
(defmethod expression-pql IAttributeExpression [expression]
  (.getAttributeName expression))

;; IOperatorExpression
(def ^{:private true} operator-name-map {"*" "MULTIPLY",
                                         "/" "DIVIDE",
                                         "%" "MODULO",
                                         "+" "PLUS",
                                         "-" "MINUS",
                                         "<" "LT",
                                         "<=" "LE",
                                         ">" "GT",
                                         ">=" "GE",
                                         "==" "EQ",
                                         "=" "EQ",
                                         "!=" "NE",
                                         "<>" "NE"})
(def ^{:private true} operator-allows-unary #{"+" "-"})

(defmethod expression-pql IOperatorExpression [expression]
  (let [numOperands (.size (.getOperandList expression))
        ; If operator name is a comparison operator, convert it to functional form, else use the operator name.
        ; We don't apply this transformation for unary operators
        operatorName (if (> (.size (.getOperandList expression)) 1) (get operator-name-map (.getOperatorName expression) (.getOperatorName expression)) (.getOperatorName expression))

        ; Handle open/close parens for unary operators +/-
        openBrace (if (and (== (.size (.getOperandList expression)) 1) (contains? operator-allows-unary (.getOperatorName expression))) "" "(" )
        closeBrace (if (and (== (.size (.getOperandList expression)) 1) (contains? operator-allows-unary (.getOperatorName expression))) "" ")" )
        ]
    (str (.toUpperCase operatorName) openBrace
      (if (> numOperands 0)
        ;;Create a string interposing "," between the string pql for each operand expression
        (apply str (interpose "," (map expression-pql (.getOperandList expression))))
        "")
      closeBrace))
  )

;;; Core PQL generation
(defn generate-pql [expression]
  "Generate PQL from an IExpression"

  (expression-pql expression))


;;; Java-interop for generate-pql
(defn -generatePQL [expression]
  (generate-pql expression))


