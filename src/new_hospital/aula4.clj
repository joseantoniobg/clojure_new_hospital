(ns new-hospital.aula4
  (:use clojure.pprint))

(defrecord PacienteParticular [id, nome, nascimento, situacao])
(defrecord PacientePlanoDeSaude [id, nome, nascimento, situacao, plano])

; deve-assinar-pre-autorizacao
; >= 50, não está no plano

(defn nao-eh-urgente? [paciente]
  (not= :urgente (:situacao paciente :normal)))

(defprotocol Cobravel
  (deve-assinar-pre-autorizacao? [paciente procedimento valor]))

(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (and (>= valor 50) (nao-eh-urgente? paciente))))

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (let [plano (:plano paciente)]
      (and (not (some #(= % procedimento) plano))
           (nao-eh-urgente? paciente)))))


; (let [particular (->PacienteParticular 15, "Guilherme", "2020-01-01", :normal)
;      plano (->PacientePlanoDeSaude 20, "Jose", "2001-01-01", :normal, [:raio-x, :ultrassom])]
;  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
;  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 40))
;  (pprint (deve-assinar-pre-autorizacao? plano, :raio-x, 400))
;  )

(defmulti deve-assinar-pre-autorizacao-multi? class)
(defmethod deve-assinar-pre-autorizacao-multi? PacienteParticular [paciente] true)
(defmethod deve-assinar-pre-autorizacao-multi? PacientePlanoDeSaude [paciente] false)

(let [particular (->PacienteParticular 15, "Guilherme", "2020-01-01", :normal)
      plano (->PacientePlanoDeSaude 20, "Jose", "2001-01-01", :normal, [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao-multi? particular))
  (pprint (deve-assinar-pre-autorizacao-multi? plano))
  (pprint (deve-assinar-pre-autorizacao? plano, :raio-x, 400))
  )

(defn tipo-de-autorizador [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)
        urgencia? (= :urgente situacao)]
    (if urgencia?
      :sempre-autorizado
      (class paciente))))

(defmulti deve-assinar-pre-autorizacao-do-pedido? tipo-de-autorizador)
(defmethod deve-assinar-pre-autorizacao-do-pedido? :sempre-autorizado [pedido] false)
(defmethod deve-assinar-pre-autorizacao-do-pedido? PacienteParticular [pedido] (>= (:valor pedido 0) 50))
(defmethod deve-assinar-pre-autorizacao-do-pedido? PacientePlanoDeSaude [pedido] (not (some #(= % (:procedimento pedido)) (:plano (:paciente pedido)))))

(let [particular (->PacienteParticular 15, "Guilherme", "2020-01-01", :normal)
      plano (->PacientePlanoDeSaude 20, "Jose", "2001-01-01", :normal, [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao-do-pedido? { :paciente particular, :valor 500, :procedimento :raio-x }))
  (pprint (deve-assinar-pre-autorizacao-do-pedido? { :paciente plano, :valor 500, :procedimento :raio-x }))
  )