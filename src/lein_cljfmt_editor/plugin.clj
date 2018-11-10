(ns lein-cljfmt-editor.plugin
  (:require [cljfmt.main :refer [default-options]]
            [meta-merge.core :refer [meta-merge]]
            [leinjacker.deps :as deps]))

(defn get-settings [project]
  (meta-merge default-options (:cljfmt project {})))

(defn middleware [project]
  (-> project
    (deps/add-if-missing '[cljfmt "0.6.1"])
    (update-in
      [:repl-options :init]
      (fn [i]
        `(do
           ~i
           (let [n# (create-ns 'cljfmt.editor)]
             (intern
               n# (symbol "project-settings")
               (quote
                 ~(get-settings project)))))))))
