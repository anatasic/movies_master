(ns movies-app.routes.home
  (:require [compojure.core :refer :all]
            [movies-app.views.layout :as layout]
            [movies-app.util.extract :as util] 
            [noir.response :refer [redirect]]
            [hiccup.form :refer :all]))

(defn home [movies search-term]
  (layout/common 
    [:h1 "Movies"]
    [:p "Welcome to powerful Tomatoer"]
    [:hr]
    (form-to [:post "/search"] 
             (if-not (nil? search-term)
              (text-field {:value search-term} :search-term)
              (text-field {:placeholder "Enter search term"} :search-term))
              (submit-button "Search"))
    (if-not (nil? movies)
      (list-of-movies movies)
      )
    )
  
  )


(defn list-of-movies [movies]
   (for [movie movies]
    (let [ratings (:ratings movie)]
    [:ul
     [:li (:title movie)]
     ])))
  
  (defn search-for-movies [search-term]
    (println "Searching for movies..." search-term)
    (let [links-url (util/get-links-url)
          template-url (util/get-template-url links-url)
          movie-url (util/get-movies-url template-url search-term)
          movies-data (util/get-movies-data movie-url)]
    (home movies-data search-term)))

  

  
  (defroutes home-routes
    (GET "/home" [] (home nil nil))
    (GET "/home" [movies] (home movies))
    (POST "/search" [search-term] (search-for-movies search-term)))
  
  
  
  
  
  