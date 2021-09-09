(ns new-hospital.logic
  (:require [new-hospital.model :as h.model]))

(defn agora []
  (h.model/to-ms (java.util.Date.)))
