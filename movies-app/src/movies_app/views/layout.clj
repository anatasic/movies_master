(ns movies-app.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to movies-app"]
     (include-css "/css/screen.css")
     (include-css "/css/login.css")
     ]
    [:body body]))
