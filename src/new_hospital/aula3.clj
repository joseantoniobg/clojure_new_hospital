(ns new-hospital.aula3
  (:use clojure.pprint))

(defn carrega-paciente [id]
  (println "Carregando" id)
  (Thread/sleep 1000)
  { :id id, :carregodo-em (to-ms (java.util.Date.)) })
