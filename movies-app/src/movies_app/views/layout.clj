(ns movies-app.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  (html5
    [:head
     [:title "Tomatoer"]
     (include-css "/css/screen.css")
     (include-css "/css/login.css")
     (include-css "/css/registration.css")
     (include-css "/css/home.css")
     (include-css "/css/movie.css")
     (include-css "/css/favorites.css")
     ]
    [:body body]))
