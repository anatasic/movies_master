(ns movies-app.models.database
 (:require [monger.core :as mg]
           [monger.collection :as mc])
 (:import (org.bson.types ObjectId)))

(def connection (mg/connect))

(def db (mg/get-db connection "database"))

(defn init-db []
   connection)