(ns movies-app.models.database
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.result :as rs])
  (:import (org.bson.types ObjectId)))

(def connection (mg/connect))

(def db (mg/get-db connection "database"))

(defn username-exists? [username]
  (mc/find-maps db "users" {:username username}))

(defn init-db [] 
  connection
  )

(defn create-user [first-name last-name email username password]
  (println (mc/insert db "users" {:username username :first-name first-name :last-name last-name :email email :password password}))
  )

(defn validate-password [username password]
  (mc/find-one db "users" {:username username :password password})
  )

(defn add-favorite-movie [username movie-id]
  (mc/insert db "favorites" {:username username :movie movie-id})
  )

(defn find-favorites [username]
  (mc/find-maps db "favorites" {:username username})
  )