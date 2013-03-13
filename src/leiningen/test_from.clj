(ns leiningen.test-from
  (:refer-clojure :exclude [test])
  (:require [leiningen.test :refer [test]]
            [robert.hooke :refer [add-hook]]))

(defn test-from [project & tests]
  (robert.hooke/add-hook #'leiningen.test/form-for-testing-namespaces
                         (fn [f nses & args]
                           (let [from (symbol (first tests))
                                 nses (drop-while #(not= from %) nses)]
                             (if (seq nses)
                               (apply f nses args)
                               (require from)))))
  (apply test project (rest tests)))
