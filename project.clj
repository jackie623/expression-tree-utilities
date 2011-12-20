(defproject expression-tree-utils "1.1"
  :description "ExpressionTree utilities"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [com.stuartsierra/lazytest "2.0.0-SNAPSHOT"]]
  :repositories {"stuartsierra-releases" "http://stuartsierra.com/maven2"
                 "stuartsierra-snapshots" "http://stuartsierra.com/m2snapshots"}
  :aot [com.physion.ovation.expression_tree.utils.core])
