(ns new-hospital.aula5
  (:use clojure.pprint))

(defn tipo-de-autorizador [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)]
    (cond (= :urgente situacao) :sempre-autorizado
          (contains? paciente :plano) :plano-de-saude
          :else :credito-minimo)))

(defmulti deve-assinar-pre-autorizacao? tipo-de-autorizador)

(defmethod deve-assinar-pre-autorizacao? :sempre-autorizado [pedido] false)

(defmethod deve-assinar-pre-autorizacao? :plano-de-saude [pedido]
  (not (some #(= % (:procedimento pedido)) (:plano (:paciente pedido)))))

(defmethod deve-assinar-pre-autorizacao? :credito-minimo [pedido]
  (>= (:valor pedido 0) 50))

(let [particular {:id 15, :nome "Guilherme", :nascimento "2020-01-01", :situacao :normal}
      plano {:id 20, :nome "Jose", :nascimento "2001-01-01", :situacao :normal, :plano [:raio-x, :ultrassom]}]
  (pprint (deve-assinar-pre-autorizacao? { :paciente particular, :valor 500, :procedimento :raio-x }))
  (pprint (deve-assinar-pre-autorizacao? { :paciente plano, :valor 500, :procedimento :raio-x }))
  )