(ns movies-app.routes.movie
(:require [compojure.core :refer :all]
[movies-app.views.layout :as layout]
[hiccup.form :refer :all]))

(defn movie []
  (layout/common 
    [:h1 "Movies"]
[:p "Welcome to my guestbook"]
[:hr]
[:form
[:p "Name:"]
[:input]
[:p "Message:"]
[:textarea {:rows 10 :cols 40}]]))

(defroutes movie-routes
  (GET "/" [] (movie)))





