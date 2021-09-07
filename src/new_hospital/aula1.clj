(ns new-hospital.aula1
  (:use clojure.pprint))

(defn adiciona-paciente
  [pacientes paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente não possui id", {:paciente paciente})))
  )

(defn testa []
  (let [pacientes {}
        guilherme {:id 15
                   :nome "guilherme"
                   :nascimento "1981-09-18"}
        daniela {:id 20
                 :nome "Daniela"
                 :nascimento "1994-09-24"}
        jose { :nome "José"
               :nascimento "1994-02-10"}]
    (pprint (adiciona-paciente pacientes guilherme))
    (pprint (adiciona-paciente pacientes daniela))
    (pprint (adiciona-paciente pacientes jose))

    ))

(testa)