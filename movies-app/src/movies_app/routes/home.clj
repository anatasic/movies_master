(ns movies-app.routes.home
(:require [compojure.core :refer :all]
[movies-app.views.layout :as layout]
[hiccup.form :refer :all]))

(defn home []
  (layout/common 
    [:h1 "Movies"]
[:p "Welcome to my guestbook"]
[:hr]
[:form
[:p "Name:"]
[:input]
[:p "Message:"]
[:textarea {:rows 10 :cols 40}]]))

(defroutes home-routes
  (GET "/home" [] (home)))





