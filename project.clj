(defproject expression-tree-utils "1.1"
  :description "ExpressionTree utilities"

  :dependencies [[org.clojure/clojure "1.3.0"]]

  :dev-dependencies [[com.stuartsierra/lazytest "2.0.0-SNAPSHOT" :exclusions [org.clojure/clojure]]
                     [lein-lazytest "1.0.1" :exclusions [org.clojure/clojure]]]

  :repositories {"stuartsierra-releases" "http://stuartsierra.com/maven2"
                 "stuartsierra-snapshots" "http://stuartsierra.com/m2snapshots"}

  :aot [com.physion.ovation.expression.utils.core]

  :lazytest-path ["src" "test"])
