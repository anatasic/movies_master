(ns movies-app.routes.home
(:require [compojure.core :refer :all]
[movies-app.views.layout :as layout]
[hiccup.form :refer :all]))

(defn home []
  (layout/common 
    [:h1 "Application for movies"]
[:p "Welcome "]
[:hr]
))

(defroutes home-routes
  (GET "/" [] (home)))

