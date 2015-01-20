(ns movies-app.routes.home
  (:require [compojure.core :refer :all]
            [movies-app.views.layout :as layout]
            [movies-app.util.extract :as util]
            [noir.session :as session]
            [noir.response :refer [redirect]]
            [hiccup.form :refer :all]))
  

(defn list-of-movies [movies]
  (for [movie movies]
    (let [ ratings (:ratings movie)
          posters (:posters movie)]
      [:div.movie
       (form-to [:get (str "/movie&" (:id movie)) ]
       [:img {:src (:thumbnail posters)}]
       [:p (:title movie)]
       [:p "Audience rating: " (:audience_score ratings)]       
       [:p "Criticts rating: " (:critics_score ratings)]
       (submit-button "Details"))
       [:hr]
       
       ]))
  )

(defn home [movies search-term page]
  
  (layout/common 
    [:h1 "Movies"]
    [:p "Welcome to powerful Tomatoer"]
    [:hr]
    
    (form-to [:post "/search"] 
             (if-not (nil? search-term)
               (text-field {:value search-term} :search-term)
               (text-field {:placeholder "Enter search term"} :search-term))
             (submit-button "Search"))
    (if-not (or (nil? movies) (empty? movies))
      (do
        (layout/common 
          (list-of-movies movies)
         ;; (println (java.lang.Long/parseLong page))
          (if-not  (> (java.lang.Integer/parseInt page) 1) 
            (do
              (let [page-no  (session/flash-get :page-no)
                    ]
                (layout/common 
                  (if (> (java.lang.Integer/parseInt page) 1)
                    (form-to [:get (str "/search&" (dec (java.lang.Integer/parseInt page)) "&" search-term) ]
                             
                             (submit-button "Previous" )
                             ))
                  (if (> (count movies) 9)
                    (form-to [:get (str "/search&" (inc (java.lang.Integer/parseInt page)) "&" search-term) ]
                             (submit-button "Next" )                             
                             )
                    ) 
                  )
                )              
              )
            )
          
          (if (= page 1)     
            (do               
              (form-to [:get (str "/search&" 2 "&" search-term) ]
                       (submit-button "Next page" ))            
              )
            )         
          )
        )
      )
    (if (or (nil? movies) (empty? movies))
      (layout/common
        [:p "Sorry, there is no more movies :("]
        )
      )    
    )
  )

(defn search-for-movies [search-term page]
  (println "Searching for movies..." search-term)
  (let [links-url (util/get-links-url)
        template-url (util/get-template-url links-url)
        movie-url (util/get-movies-url template-url search-term page)
        movies-data (util/get-movies-data movie-url)]
    
    
    (home movies-data search-term page)))




(defroutes home-routes
  (GET "/home" [] (home nil nil 1))
  (GET "/search&:page&:search-term" [page search-term]  (search-for-movies search-term page))
  (POST "/search" [search-term] (search-for-movies search-term "1") ))





