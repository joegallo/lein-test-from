(ns leiningen.test-from
  (:refer-clojure :exclude [test])
  (:use [leiningen.test :only [form-for-testing-namespaces test]]
        [robert.hooke :only [add-hook]]))

(def old-form form-for-testing-namespaces)

(defn test-from [project & tests]
  (let [from (symbol (first tests))]
    (binding [form-for-testing-namespaces
              (fn [nses rf & selectors]
                (let [nses (drop-while #(not= from %) nses)]
                  (if (seq nses)
                    (apply old-form nses rf selectors)
                    (require from))))]
      (apply test project (rest tests)))))
