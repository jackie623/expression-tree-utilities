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
  (.getValue expression))

;; ClassLiteralValueExpression
(defmethod value-expression-pql IClassLiteralValueExpression [expression]
  (str "class:" (.getValue expression)))

;; TimeLiteralValueExpression
(defmethod value-expression-pql ITimeLiteralValueExpression [expression]
  (let [calendarDate (GregorianCalendar.)]
    (do
      (.setTime calendarDate (.getValue expression))
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
        ))))



;;; Multimethod dispatch for IExpression
(defmulti expression-pql class)

;; ILiteralValueExpression
(defmethod expression-pql ILiteralValueExpression [expression]
  (value-expression-pql expression))

;; IAttributeExpression
(defmethod expression-pql IAttributeExpression [expression]
  (.getAttributeName expression))

;; IOperatorExpression
(defmethod expression-pql IOperatorExpression [expression]
  (let [numOperands (.size (.getOperandList expression))]
    (str (.toUpperCase (.getOperatorName expression)) "("
      (if (> numOperands 0)
        ;;Create a string interposing "," between the string pql for each operand expression
        (apply str (interpose "," (map expression-pql (.getOperandList expression))))
        "")
      ")"))
  )

;;; Core PQL generation
(defn generate-pql [expression]
  "Generate PQL from an IExpression"

  (expression-pql expression))


;;; Java-interop for generate-pql
(defn -generatePQL [expression]
  (generate-pql expression))


